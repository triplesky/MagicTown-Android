package com.bekids.gogotown.unity.bridge.strategy

import com.bekids.gogotown.base.utils.IHEventBus
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.model.bean.ProductListHolder
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
import com.bekids.gogotown.unity.bridge.bean.*
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihuman.sdk.lib.network.utils.IHNetworkUtils
import com.ihuman.sdk.lib.pay.IHPayComponent
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONObject

/**
 ***************************************
 * 项目名称: gogomarket
 * @Author ll
 * 创建时间: 2022/7/28    2:21 下午
 * 用途
 ***************************************

 */
@RegisterUnityMethod(
    MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST,
    MessageConstants.MESSAGE_PAY_CREATE_ORDER_AND_PURCHASE_WITH_PRODUCT_ID,
    MessageConstants.MESSAGE_PAY_RESTORE_PURCHASE,
    MessageConstants.MESSAGE_PAY_PURCHASE_WITH_PRODUCT_ID,
    MessageConstants.MESSAGE_PAY_CLEAR_PRODUCT_LIST,
    MessageConstants.MESSAGE_PRODUCT_PRICE_WITH_RATE,
    MessageConstants.MESSAGE_PAY_FETCH_USER_STATUS,
    MessageConstants.MESSAGE_PRODUCT_PRICE_WITH_PRICE
)
class OrderStrategy : BaseAbstractStrategy() {
    var blockIdMap = HashMap<String?, String?>()
    override fun process(method: String?, jsonArray: JSONArray?, blockId: String?): String {
        IHEventBus.instance?.let {
            if (!it.isRegistered(this)) {
                it.register(this)
            }
        }
        blockIdMap[method] = blockId
        var result = ""
        when (method) {
            MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST -> {
                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST, null))
            }

            MessageConstants.MESSAGE_PAY_FETCH_USER_STATUS -> {
                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_FETCH_USER_STATUS, null))
            }

            //old
            MessageConstants.MESSAGE_PAY_CREATE_ORDER_AND_PURCHASE_WITH_PRODUCT_ID -> {
                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID, jsonArray?.getString(0)))
            }

            //new visitor
            MessageConstants.MESSAGE_PAY_PURCHASE_WITH_PRODUCT_ID -> {
                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_PURCHASE_WITH_PRODUCT_ID, jsonArray?.getString(0)))
            }

            MessageConstants.MESSAGE_PAY_RESTORE_PURCHASE -> {
                IHEventBus.instance?.post(BusMsg(BusMsg.PAY_RESTORE_PURCHASE, null))
            }

            MessageConstants.MESSAGE_PAY_CLEAR_PRODUCT_LIST -> {
                ProductListHolder.clearProduct()
            }
            MessageConstants.MESSAGE_PRODUCT_PRICE_WITH_RATE -> {
                var list = ProductListHolder.getSkuInfo()
                list?.let {
                    for ((i, item) in it.withIndex()) {
                        if (item.sku == jsonArray!![1]) {
                            var scal = 0.0
                            scal = jsonArray[0].toString().toDouble()
                            var jsonObject = JSONObject()
                            jsonObject.put("type", "obj")
                            jsonObject.put("value", item.priceStringWithRate(scal, 2))
                            result = generateNormalJson(jsonObject)
                            break
                        }
                    }
                }

            }
            MessageConstants.MESSAGE_PRODUCT_PRICE_WITH_PRICE -> {
                var list = ProductListHolder.getSkuInfo()
                list?.let {
                    for ((i, item) in it.withIndex()) {
                        if (item.sku == jsonArray!![1]) {
                            var scal = 0.0
                            scal = jsonArray[0].toString().toDouble()
                            var jsonObject = JSONObject()
                            jsonObject.put("type", "obj")
                            jsonObject.put("value", item.priceStringWithPrice(scal))
                            result = generateNormalJson(jsonObject)
                            break
                        }
                    }
                }
            }
        }
        return result
    }

    @Subscribe
    fun onEvent(event: BusMsg<*>) {
        when (event.code) {
            BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST_COMPLETE -> {
                if (event.data == null) {
                    if (IHNetworkUtils.isConnected()) {
                        callUnity(
                            MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST,
                            generateNormalFailedJson(-1, "fetch product failed"),
                            blockIdMap[MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST],
                            MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST
                        )
                    } else{
                        callUnity(
                            MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST,
                            generateNormalFailedJson(-1, "The Internet connection appears to be offline"),
                            blockIdMap[MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST],
                            MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST
                        )
                    }
                } else {
                    var result = event.data as MutableList<PurchaseBean>
                    val jsonObject = JSONObject()
                    jsonObject.put("code", 0)
                    jsonObject.put("finished", true)
                    val type = object :  TypeToken<MutableList<PurchaseBean>>() {}.type
                    jsonObject.put("product_list", JSONArray(Gson().toJson(result, type)))
                    callUnity(
                        MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST,
                        generateNormalJson(jsonObject),
                        blockIdMap[MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST],
                        MessageConstants.MESSAGE_PAY_FETCH_ALL_PACK_PRODUCT_LIST
                    )
                }
            }


            BusMsg.PAY_FETCH_USER_STATUS_COMPLETE -> {
                if (event.data != null) {
                    var data = event.data as CommonBean
                    val jsonObject = JSONObject()
                    jsonObject.put("code", data.code)
                    jsonObject.put("message", data.msg)
                    callUnity(
                        MessageConstants.MESSAGE_PAY_FETCH_USER_STATUS,
                        generateNormalJson(jsonObject),
                        blockIdMap[MessageConstants.MESSAGE_PAY_FETCH_USER_STATUS],
                        MessageConstants.MESSAGE_PAY_FETCH_USER_STATUS
                    )
                }

            }

            BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID_COMPLETE -> {
                if ((event.data as Payment).code == 0){



                    var payment = JSONObject()
                    payment.put("order_id", (event.data as Payment).payments.orderId)
                    callUnity(
                        MessageConstants.MESSAGE_PAY_CREATE_ORDER_AND_PURCHASE_WITH_PRODUCT_ID,
                        generateNormalJson(payment),
                        blockIdMap[MessageConstants.MESSAGE_PAY_CREATE_ORDER_AND_PURCHASE_WITH_PRODUCT_ID],
                        MessageConstants.MESSAGE_PAY_CREATE_ORDER_AND_PURCHASE_WITH_PRODUCT_ID
                    )

                    val jsonObject = JSONObject()

                    var arr = JSONArray()
                    var payment1 = JSONObject()
                    payment1.put("status", (event.data as Payment).payments.status)
                    payment1.put("deliverStatus", (event.data as Payment).payments.deliverStatus)
                    var product = JSONObject()
                    product.put("product_id", (event.data as Payment).payments.product.product_id)
                    payment1.put("product", product)
                    arr.put(payment1)
                    jsonObject.put("payments", arr)
                    callUnity(
                        MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_SUCCESS,
                        generateNormalJson(jsonObject),
                        "",
                        MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_SUCCESS
                    )

                }else{
                    var payment = JSONObject()
                    var arr = JSONArray()
                    var product = JSONObject()
                    if (event.data != null) {
                        product.put("product_id", (event.data as Payment).payments.product.product_id)
                        var payment1 = JSONObject()
                        payment1.put("product", product)
                        arr.put(payment1)
                    }

                    payment.put("payments", arr)
                    payment.put("code", (event.data as Payment).code)
                    payment.put("status", (event.data as Payment).code)
                    payment.put("message", "pay failed")
                    callUnity(
                        MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_FAILED,
                        generateNormalJson(payment),
                        "",
                        MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_FAILED
                    )
                }


            }

            BusMsg.PAY_RESTORE_PURCHASE_COMPLETE -> {
                if ((event.data as ResumePayBean).code == 0){

                    val jsonObject = JSONObject()

                    var arr = JSONArray()
                    var list = (event.data as ResumePayBean).product_id_list
                    if (!list.isNullOrEmpty()) {
                        for ((i,pro) in list.withIndex()){
                            var payment = JSONObject()
                            payment.put("status", 230)
                            payment.put("deliverStatus", 0)
                            var product = JSONObject()
                            product.put("product_id", pro)
                            payment.put("product", product)
                            arr.put(payment)
                        }
                        jsonObject.put("payments", arr)
                        callUnity(
                            MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_SUCCESS,
                            generateNormalJson(jsonObject),
                            "",
                            MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_SUCCESS
                        )

                    } else{
                        jsonObject.put("payments", arr)
                        callUnity(
                            MessageConstants.MESSAGE_RESTORE_PURCHASE_COMPLETE_NO_PRODUCT,
                            generateNormalJson(jsonObject),
                            "",
                            MessageConstants.MESSAGE_RESTORE_PURCHASE_COMPLETE_NO_PRODUCT
                        )
                    }

                } else {
                    val jsonObject = JSONObject()

                    var arr = JSONArray()
                    var payment = JSONObject()
                    payment.put("status", -1)
                    payment.put("deliverStatus", 0)
                    var product = JSONObject()
                    product.put("product_id", "")
                    payment.put("product", product)
                    arr.put(payment)
                    jsonObject.put("payments", arr)
                    jsonObject.put("code", -1)

                    callUnity(
                        MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_FAILED,
                        generateNormalJson(jsonObject),
                        "",
                        MessageConstants.MESSAGE_PAY_PURCHASE_ALL_PACK_FAILED
                    )
                }

            }

        }
    }
}