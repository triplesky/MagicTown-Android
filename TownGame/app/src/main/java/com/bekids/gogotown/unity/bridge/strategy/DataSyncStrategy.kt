package com.bekids.gogotown.unity.bridge.strategy

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.bekids.gogotown.MainApplication
import com.bekids.gogotown.base.utils.IHEventBus
import com.bekids.gogotown.bean.LoginInfoHolder
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.ext.toast
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
import com.bekids.gogotown.unity.bridge.bean.MessageConstants
import com.bekids.gogotown.unity.bridge.bean.MogoData
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy
import com.ihuman.sdk.lib.datasync.IHDataSyncComponent
import com.ihuman.sdk.lib.datasync.callback.IHumanSyncCallback
import com.ihuman.sdk.lib.model.FailMessage
import com.ihuman.sdk.lib.utils.IHDataConvertUtils
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File


/**
 ***************************************
 * 项目名称: bekids Town
 * @Author ll
 * 创建时间: 2023/1/3    7:31 下午
 * 用途
 ***************************************

 */
@RegisterUnityMethod(
    MessageConstants.MESSAGE_DATA_SYNC_FLUSH_DATA_WITH_NAME,MessageConstants.MESSAGE_DATA_SYNC_FLUSH_ALL_DATA,MessageConstants.MESSAGE_DATA_SYNC_FORCE_FLUSH_DATA,
    MessageConstants.MESSAGE_DATA_SYNC_ADD_NUMBER_TO_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_ADD_STRING_TO_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_ADD_OBJECTS_TO_SET_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_CHECK_NUMBER_EXISTS_TO_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_CHECK_STRING_EXISTS_TO_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_DELETE_ALL_OBJECTS_TO_DICT_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_DELETE_ALL_OBJECTS_TO_SET_FOR_NAME, MessageConstants.MESSAGE_DATA_SYNC_DELETE_NUMBER_TO_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_DELETE_OBJECTS_FOR_NAME_FOR_KEY_ARRAY,
    MessageConstants.MESSAGE_DATA_SYNC_DELETE_OBJECTS_TO_SET_FOR_NAME, MessageConstants.MESSAGE_DATA_SYNC_DELETE_OBJECT_FOR_NAME_FOR_LEY, MessageConstants.MESSAGE_DATA_SYNC_DELETE_STRING_TO_SET_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_DICT_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_DICT_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_SET_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_FETCH_NUMBER_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECTS_FOR_NAME_FOR_KEY_ARRAY,
    MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECT_FOR_NAME_FOR_KEY,MessageConstants.MESSAGE_DATA_SYNC_FETCH_STRING_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_INCREASE_NUMBER_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_INCREASE_NUMBER_FOR_NAME_FOR_KEY, MessageConstants.MESSAGE_DATA_SYNC_REGISTER_WITH_SCHEMA,MessageConstants.MESSAGE_DATA_SYNC_REMOVE_DATA_FROM_CACHE_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_REPLACE_ALL_DICTIONARY_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_REPLACE_NUMBER_FOR_NAME_FOR_KEY,MessageConstants.MESSAGE_DATA_SYNC_REPLACE_OBJECTS_TO_SET_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_REPLACE_OBJECT_FOR_NAME_FOR_KEY,MessageConstants.MESSAGE_DATA_SYNC_REPLACE_STRING_FOR_NAME_FOR_KEY,MessageConstants.MESSAGE_DATA_SYNC_SET_ALL_DICTIONARY_FOR_NAME,
    MessageConstants.MESSAGE_DATA_SYNC_SET_NUMBER_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_SET_NUMBER_FOR_NAME_FOR_KEY,MessageConstants.MESSAGE_DATA_SYNC_SET_OBJECTS_FOR_NAME_FOR_KEY_ARRAY,
    MessageConstants.MESSAGE_DATA_SYNC_SET_OBJECT_FOR_NAME_FOR_KEY,MessageConstants.MESSAGE_DATA_SYNC_SET_STRING_FOR_NAME,MessageConstants.MESSAGE_DATA_SYNC_SET_STRING_FOR_NAME_FOR_KEY,
    MessageConstants.MESSAGE_DATA_SYNC_TEST_INVALID_DATA_VERSION,MessageConstants.MESSAGE_SAVE_IMAGE
)
class DataSyncStrategy : BaseAbstractStrategy() {
    override fun process(method: String?, jsonArray: JSONArray?, blockId: String?): String {
        IHEventBus.instance?.let {
            if (!it.isRegistered(this)) {
                it.register(this)
            }
        }
        var result =""
        when (method) {
            MessageConstants.MESSAGE_DATA_SYNC_FLUSH_DATA_WITH_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().flushDataWithName(jsonArray!![0].toString(), object :
                    IHumanSyncCallback{
                    override fun onFailure(failMessage: FailMessage?) {
                        failMessage?.let {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FLUSH_DATA_WITH_NAME,
                                generateNormalFailedJson(it.code, it.message),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FLUSH_DATA_WITH_NAME)
                        }

                    }

                    override fun onSuccess(
                        resultMap: MutableMap<Any?, Any?>?,
                        resultString: String?,
                        code: Int
                    ) {
                        callUnity(MessageConstants.MESSAGE_DATA_SYNC_FLUSH_DATA_WITH_NAME,
                            generateNormalFailedJson(code, resultString),id,
                            MessageConstants.MESSAGE_DATA_SYNC_FLUSH_DATA_WITH_NAME)
                    }

                })
            }
            MessageConstants.MESSAGE_DATA_SYNC_FLUSH_ALL_DATA -> {
                var id =blockId
                IHDataSyncComponent.getInstance().flushAllDataWithBlock(object : IHumanSyncCallback{
                    override fun onFailure(failMessage: FailMessage?) {
                        failMessage?.let {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FLUSH_ALL_DATA,
                                generateNormalFailedJson(it.code, it.message),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FLUSH_ALL_DATA)
                        }
                    }

                    override fun onSuccess(
                        resultMap: MutableMap<Any?, Any?>?,
                        resultString: String?,
                        code: Int
                    ) {
                        callUnity(MessageConstants.MESSAGE_DATA_SYNC_FLUSH_ALL_DATA,
                            generateNormalFailedJson(code, resultString),id,
                            MessageConstants.MESSAGE_DATA_SYNC_FLUSH_ALL_DATA)
                    }

                })
            }

            MessageConstants.MESSAGE_DATA_SYNC_FORCE_FLUSH_DATA -> {
                var id =blockId
                IHDataSyncComponent.getInstance().forceFlushData(jsonArray!![0].toString(), object :
                    IHumanSyncCallback{
                    override fun onFailure(failMessage: FailMessage?) {
                        failMessage?.let {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FORCE_FLUSH_DATA,
                                generateNormalFailedJson(it.code, it.message),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FORCE_FLUSH_DATA)
                        }
                    }

                    override fun onSuccess(
                        resultMap: MutableMap<Any?, Any?>?,
                        resultString: String?,
                        code: Int
                    ) {
                        callUnity(MessageConstants.MESSAGE_DATA_SYNC_FORCE_FLUSH_DATA,
                            generateNormalFailedJson(code, resultString),id,
                            MessageConstants.MESSAGE_DATA_SYNC_FORCE_FLUSH_DATA)
                    }

                })
            }

            MessageConstants.MESSAGE_DATA_SYNC_ADD_NUMBER_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.addObject(jsonArray[1])?.let {
                        result = generateBooleanJson(it)
                    }


            }

            MessageConstants.MESSAGE_DATA_SYNC_ADD_STRING_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.addObject(jsonArray[1])?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_ADD_OBJECTS_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.addObjects(jsonArray[1] as Array<out Any>?)?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_CHECK_NUMBER_EXISTS_TO_SET_FOR_NAME -> {
                var id = blockId
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.checkObjectExist(jsonArray[1], object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_CHECK_NUMBER_EXISTS_TO_SET_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_CHECK_NUMBER_EXISTS_TO_SET_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_CHECK_NUMBER_EXISTS_TO_SET_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_CHECK_NUMBER_EXISTS_TO_SET_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_CHECK_STRING_EXISTS_TO_SET_FOR_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.checkObjectExist(jsonArray[1], object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_CHECK_STRING_EXISTS_TO_SET_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_CHECK_STRING_EXISTS_TO_SET_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_CHECK_STRING_EXISTS_TO_SET_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_CHECK_STRING_EXISTS_TO_SET_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_DELETE_ALL_OBJECTS_TO_DICT_FOR_NAME -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.deleteAllObjects()?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_DELETE_ALL_OBJECTS_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.deleteAllObjects()?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_DELETE_NUMBER_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.deleteObject(jsonArray[1])?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_DELETE_OBJECTS_FOR_NAME_FOR_KEY_ARRAY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.deleteObjectsForKeys(jsonArray[1] as Array<out String>?)?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_DELETE_OBJECTS_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.deleteObjects(jsonArray[1] as Array<out Any>?)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_DELETE_OBJECT_FOR_NAME_FOR_LEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.deleteObjectForKey(jsonArray[1] as String?)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_DELETE_STRING_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.deleteObject(jsonArray[1])?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_DICT_FOR_NAME -> {

                var id =blockId
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.fetchAllObjectsWithBlock(object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_DICT_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_DICT_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_DICT_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_DICT_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_SET_FOR_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.fetchAllObjectsWithBlock(object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_SET_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_SET_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_SET_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_ALL_OBJECTS_IN_SET_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_DICT_FOR_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.fetchLengthWithBlock(object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_DICT_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_DICT_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_DICT_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_DICT_FOR_NAME)
                        }

                    })
            }


            MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_SET_FOR_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.fetchLengthWithBlock(object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_SET_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_SET_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_SET_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_LENGTH_IN_SET_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_FETCH_NUMBER_FOR_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().numberWithName(jsonArray!![0].toString())
                    ?.fetchNumberWithBlock(object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_NUMBER_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_NUMBER_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_NUMBER_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_NUMBER_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECTS_FOR_NAME_FOR_KEY_ARRAY -> {
                var id =blockId
                val array: JSONArray = jsonArray?.get(1) as JSONArray

                val strArr = arrayOfNulls<String>(array.length())
                for (i in strArr.indices) {
                    strArr[i] = array[i] as String
                }
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.fetchObjectsForKeys(strArr,object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECTS_FOR_NAME_FOR_KEY_ARRAY,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECTS_FOR_NAME_FOR_KEY_ARRAY)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECTS_FOR_NAME_FOR_KEY_ARRAY,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECTS_FOR_NAME_FOR_KEY_ARRAY)
                        }

                    })
            }


            MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECT_FOR_NAME_FOR_KEY -> {
                var id =blockId
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.fetchObjectForKey(jsonArray[1] as String?,object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECT_FOR_NAME_FOR_KEY,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECT_FOR_NAME_FOR_KEY)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECT_FOR_NAME_FOR_KEY,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_OBJECT_FOR_NAME_FOR_KEY)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_FETCH_STRING_FOR_NAME -> {
                var id =blockId
                IHDataSyncComponent.getInstance().stringWithName(jsonArray!![0].toString())
                    ?.fetchStringWithBlock(object : IHumanSyncCallback{
                        override fun onFailure(failMessage: FailMessage?) {
                            failMessage?.let {
                                callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_STRING_FOR_NAME,
                                    generateNormalDataJson(it.code, it.message, it.result),id,
                                    MessageConstants.MESSAGE_DATA_SYNC_FETCH_STRING_FOR_NAME)
                            }
                        }

                        override fun onSuccess(
                            resultMap: MutableMap<Any?, Any?>?,
                            resultString: String?,
                            code: Int
                        ) {
                            callUnity(MessageConstants.MESSAGE_DATA_SYNC_FETCH_STRING_FOR_NAME,
                                generateNormalDataJson(code, resultString, resultMap),id,
                                MessageConstants.MESSAGE_DATA_SYNC_FETCH_STRING_FOR_NAME)
                        }

                    })
            }

            MessageConstants.MESSAGE_DATA_SYNC_INCREASE_NUMBER_FOR_NAME -> {
                //TODO
//                IHDataSyncComponent.getInstance().numberWithName(jsonArray!![0].toString())
//                    ?.increaseNumber(jsonArray[1] as Double)?.let {
//                        result = generateBooleanJson(it)
//                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_INCREASE_NUMBER_FOR_NAME_FOR_KEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.increaseNumberForKey(jsonArray[1] as String?, jsonArray[2] as Double)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_REGISTER_WITH_SCHEMA -> {
                IHDataSyncComponent.getInstance().registerWithSchema(jsonArray!![0].toString())
                LoginInfoHolder.sdkLoginStateUpdate()
            }

            MessageConstants.MESSAGE_DATA_SYNC_REMOVE_DATA_FROM_CACHE_FOR_NAME -> {

            }


            MessageConstants.MESSAGE_DATA_SYNC_REPLACE_ALL_DICTIONARY_FOR_NAME -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.replaceWithObject(IHDataConvertUtils.jsonToMap(jsonArray[1].toString()) as MutableMap<String, Any>?)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_REPLACE_NUMBER_FOR_NAME_FOR_KEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.replaceWithObject(jsonArray[1] as String?,jsonArray[2])?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_REPLACE_OBJECTS_TO_SET_FOR_NAME -> {
                IHDataSyncComponent.getInstance().setWithName(jsonArray!![0].toString())
                    ?.replaceWithObjects(arrayOf(jsonArray[1]))?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_REPLACE_OBJECT_FOR_NAME_FOR_KEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.replaceWithObject(jsonArray[1] as String?,jsonArray[2])?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_REPLACE_STRING_FOR_NAME_FOR_KEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.replaceWithObject(jsonArray[1] as String?,jsonArray[2])?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_SET_ALL_DICTIONARY_FOR_NAME -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.setObject(IHDataConvertUtils.jsonToMap(jsonArray[1].toString()) as MutableMap<String, Any>?)?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_SET_NUMBER_FOR_NAME -> {
                IHDataSyncComponent.getInstance().numberWithName(jsonArray!![0].toString())
                    ?.setNumber(jsonArray[1] as Double)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_SET_NUMBER_FOR_NAME_FOR_KEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.setObject(jsonArray[1] as String?,jsonArray[2])?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_SET_OBJECTS_FOR_NAME_FOR_KEY_ARRAY -> {
                val array: JSONArray = jsonArray?.get(1) as JSONArray

                val strArr = arrayOfNulls<String>(array.length())
                for (i in strArr.indices) {
                    strArr[i] = array[i] as String
                }

                val array1: JSONArray = jsonArray?.get(2) as JSONArray


                var map = arrayOfNulls<MutableMap<Any?, Any?>>(array1.length())
                for (i in map.indices) {
                    map[i] = IHDataConvertUtils.jsonToMap(array1[i].toString()) //{"s_theme_id":"pul_jichu","s_uid":"281474976725827","n_unlocked_seq":6}
                }

                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.setObjects(strArr,
                        map
                    )?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_SET_OBJECT_FOR_NAME_FOR_KEY -> {
                val obj: JSONObject = jsonArray?.get(2) as JSONObject
                var map = IHDataConvertUtils.jsonToMap(obj.toString())
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.setObject(jsonArray[1] as String?,map)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_SET_STRING_FOR_NAME -> {
                IHDataSyncComponent.getInstance().stringWithName(jsonArray!![0].toString())
                    ?.setString(jsonArray[1] as String?)?.let {
                        result = generateBooleanJson(it)
                    }
            }

            MessageConstants.MESSAGE_DATA_SYNC_SET_STRING_FOR_NAME_FOR_KEY -> {
                IHDataSyncComponent.getInstance().mapWithName(jsonArray!![0].toString())
                    ?.setObject(jsonArray[1] as String?, jsonArray[2])?.let {
                        result = generateBooleanJson(it)
                    }
            }


            MessageConstants.MESSAGE_DATA_SYNC_TEST_INVALID_DATA_VERSION -> {
                IHDataSyncComponent.getInstance().testInvalidDataVersion()
            }

            MessageConstants.MESSAGE_SAVE_IMAGE ->{
                saveImage(jsonArray?.get(0) as String, blockId)
            }

        }
        return result
    }

    private fun saveImage(path: String, blockId: String?):Boolean {
        val file = File(path)
        // 把文件插入到系统图库
        try {
            val index: Int = path.lastIndexOf("/")
            //截取字符串
            //截取字符串
            val name: String = path.substring(index + 1)
            val insertImage: String = MediaStore.Images.Media.insertImage(
                MainApplication.mainActivity.contentResolver,
                file.absolutePath, name, null)
            //            // 通知图库更新
            val intent = Intent()
            intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
            intent.data = Uri.fromFile(file)
            MainApplication.mainActivity.sendBroadcast(intent)
            //MainApplication.mainActivity.toast("success")
            callUnity(MessageConstants.MESSAGE_SAVE_IMAGE,
                "",blockId,
                MessageConstants.MESSAGE_SAVE_IMAGE)
            return true
        } catch (e: java.lang.Exception) {
            MainApplication.mainActivity.toast("fail")
            e.printStackTrace()
        }
        return false
    }

    fun generateNormalDataJson(errCode: Int, errMsg: String?, data_obj : Any?): String? {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("code", errCode)
            jsonObject.put("message", errMsg)
            if (data_obj is MutableMap<*, *>?) {
                jsonObject.put("data_obj", JSONObject(IHDataConvertUtils.mapToJson(data_obj)))
            } else{
                jsonObject.put("data_obj", data_obj)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }

    @Subscribe
    fun onEvent(event: BusMsg<*>) {
        when (event.code) {
            BusMsg.MONGO_MERGE_WITH_DATA -> {
                if (event.data != null) {
                    var result = event.data as MogoData
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("data", JSONObject(IHDataConvertUtils.mapToJson(result.data)))
                        jsonObject.put("need_force_flush", result.needForceFlush)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    callUnity(MessageConstants.MESSAGE_DATA_SYNC_MONGO_MERGE_WITH_DATA,
                        jsonObject.toString(),"",
                        MessageConstants.MESSAGE_DATA_SYNC_MONGO_MERGE_WITH_DATA)
                }
            }

        }
    }

}