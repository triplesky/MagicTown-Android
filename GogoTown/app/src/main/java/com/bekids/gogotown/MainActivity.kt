package com.bekids.gogotown

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bekids.gogotown.base.BaseApplication
import com.bekids.gogotown.base.utils.IHEventBus
import com.bekids.gogotown.eventbus.BusMsg
import com.bekids.gogotown.ext.toast
import com.bekids.gogotown.unity.bridge.NativeCommunicatorForAndroid
import com.bekids.gogotown.unity.bridge.NativeCommunicatorForAndroidListener
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod
import com.bekids.gogotown.unity.bridge.bean.MessageConstants
import com.bekids.gogotown.unity.bridge.bean.MogoData
import com.bekids.gogotown.unity.bridge.dispatcher.UnityMessageDispatcher
import com.bekids.gogotown.unity.bridge.interf.IUnityView
import com.bekids.gogotown.unity.bridge.log.UnityBridgeLogPrinter
import com.bekids.gogotown.util.file.AssetsFileUtil
import com.bekids.gogotown.viewmodel.LoginViewModelImpl
import com.ihuman.library.accountsdk.AccountHelp
import com.ihuman.media.player.bridge.IHMediaPlayerBridge
import com.ihuman.sdk.lib.datasync.callback.IHumanDataMergeCallback
import com.unity3d.player.IUnityPlayerLifecycleEvents
import com.unity3d.player.UnityPlayer
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@RegisterUnityMethod(
    MessageConstants.MESSAGE_GET_API,
    MessageConstants.MESSAGE_POST_API
)
class MainActivity : AppCompatActivity() , IUnityPlayerLifecycleEvents, IHumanDataMergeCallback {
    private var focusRequest: AudioFocusRequest? = null
    private val mViewModel by viewModel<LoginViewModelImpl>{ parametersOf(this) }
    private val TAG = "UnityPlayerActivity"
    protected var mUnityPlayer: UnityPlayer? = null
    var unityMessageDispatcher: UnityMessageDispatcher? = null
    private var audioManager: AudioManager? = null
    private var ihMediaPlayerBridge:IHMediaPlayerBridge? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        MainApplication.mainActivity = this
        val cmdLine = updateUnityCommandLineArguments(intent.getStringExtra("unity"))
        intent.putExtra("unity", cmdLine)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        //copyAssetsUnityFolderToInternal("program")
        connectUnityBridge()
        IHMediaPlayerBridge.loadInJava(this)

        mUnityPlayer = UnityPlayer(this, this)
        //        setContentView(mUnityPlayer);
        container.addView(mUnityPlayer)
        mUnityPlayer!!.requestFocus()
        IHEventBus.instance?.register(this)
        startObserver()
        AccountHelp.getInstance().checkResource(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
//            setAudioAttributes(AudioAttributes.Builder().run {
//                setUsage(AudioAttributes.USAGE_GAME)
//                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                build()
//            })
//            setAcceptsDelayedFocusGain(true)
//            setOnAudioFocusChangeListener(afChangeListener, handler)
//            build()
//        }

        Log.e("lldebug11", getCurrentLanguage() + "," + getCurrentLanguageUseResources())
    }

    private fun getCurrentLanguage(): String? {
        //获取系统当前使用的语言
        //设置成简体中文的时候，getLanguage()返回的是zh
        return Locale.getDefault().language
    }

    private fun getCurrentLanguageUseResources(): String? {
        /**
         * 获得当前系统语言
         */
        val locale = resources.configuration.locale
        return locale.language
    }


//    private val handler:Handler = Handler(Looper.getMainLooper())
//    private val afChangeListener: AudioManager.OnAudioFocusChangeListener =
//        object : AudioManager.OnAudioFocusChangeListener {
//            override fun onAudioFocusChange(focusChange: Int) {
//                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
//                    Log.i("lldebug audio", "AUDIOFOCUS_LOSS_TRANSIENT")
//                    // Pause playback
//                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
//                    Log.i("lldebug audio", "AUDIOFOCUS_GAIN")
//                    // Resume playback
//                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
//                    Log.i("lldebug audio", "AUDIOFOCUS_LOSS")
//                    // mAm.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
//
//                }
//            }
//        }



    fun formatDateToString(milSecond: Long, pattern: String): String {
        val date = Date(milSecond)
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        return format.format(date)
    }


    fun startObserver(){
        mViewModel?.apply {
            loginUiState.observe(this@MainActivity, Observer { loginUiState ->
                with(loginUiState){
                    userInfoBean?.let {
                        IHEventBus.instance?.post(BusMsg(BusMsg.SIGN_IN_COMPLETE,null))
                        IHEventBus.instance?.post(BusMsg(BusMsg.NOTI_USER_RIGHTS_UPDATE, null))
                    }
                    showError?.let {
                        toast(it)
                    }
                }
            })

            logoutUiState.observe(this@MainActivity, Observer { logoutUiState ->
                with(logoutUiState){
                    result?.let {
                        BaseApplication.token_invalid = 0
                        IHEventBus.instance?.post(BusMsg(BusMsg.SIGN_OUT_COMPLETE,it))
                        IHEventBus.instance?.post(BusMsg(BusMsg.NOTI_USER_RIGHTS_UPDATE, null))
                    }
                }
            })
        }
    }


    private fun copyAssetsUnityFolderToInternal(dirName: String) {
        val unityPath = getExternalFilesDir(null)!!.absolutePath

        //        if (getExternalFilesDir(null) != null) {
//            getExternalFilesDir(null).getAbsolutePath();
//        } else {
//            filesDir.absolutePath
//        }
        AssetsFileUtil.copyFilesFromAssets(this, dirName, unityPath)
    }

    private fun sendAccountInfoToUnity() {
        //向unity发送用户账户消息
//        callUnity(MessageConstants.MESSAGE_RECEIVE_USER_LOGIN_INFO,accountInfo,"","unity on create");
    }

    fun callUnity(method: String?, params: String?, blockId: String?, originMethod: String?) {
        UnityBridgeLogPrinter.getInstance().printUnityMessageCallbackInfo(
            "UnityPlayerActivity", method,
            params, blockId, originMethod
        )
        NativeCommunicatorForAndroid.shared_instance()
            .callUnity(method, params, blockId, originMethod)
    }


    private fun connectUnityBridge() {
        unityMessageDispatcher = UnityMessageDispatcher(object : IUnityView {
            override fun onWebViewClosed() {}
            override fun hideUnitySplashPage() {}
            override fun getUnityContext(): Context {
                return this@MainActivity
            }

            override fun showLoginFragment(
                phone: String,
                url: String,
                blockId: String,
                fromPage: String,
            ) {
            }

            override fun hideLoginFragment() {}
            override fun showUnityLoadingDialog() {}
            override fun hideUnityLoadingDialog() {}
            override fun getUnityMainHandler(): Handler {
                return NativeCommunicatorForAndroid.shared_instance().unityMainThreadHandler
            }

            override fun reportDownloadTask(
                url: String,
                status: Int,
                cbyte: Int,
                tbytes: Int,
                ecode: Int,
                errmsg: String,
            ) {
                NativeCommunicatorForAndroid.shared_instance()
                    .reportDownloadTask(url, status, cbyte, tbytes, ecode, errmsg)
            }
        })
        NativeCommunicatorForAndroid.shared_instance()
            .register_listener(object : NativeCommunicatorForAndroidListener {
                override fun recive_native_call_application(message: String, params: String) {
                    Log.d(TAG, "----recive_native_call_application----")
                    Log.d(TAG, "message:$message")
                    Log.d(TAG, "params:$params")
                }

                override fun recive_native_call_application_block(blockID: String, params: String) {
                    Log.d(TAG, "----recive_native_call_application_block----")
                    Log.d(TAG, "blockID:$blockID")
                    Log.d(TAG, "params:$params")
                }

                override fun recive_report_download_task_report(
                    url: String,
                    status: Int,
                    cbyte: Int,
                    tbytes: Int,
                    ecode: Int,
                    errmsg: String,
                ) {
                    Log.i("lldebug report", ecode.toString())
                }

                override fun recive_native_request_runing_state(): String {
                    return ""
                }

                override fun application_report_log(): String {
                    return ""
                }
            })
        NativeCommunicatorForAndroid.shared_instance()
            .registerMessageDispatcher(unityMessageDispatcher)
    }

    protected fun updateUnityCommandLineArguments(cmdLine: String?): String? {
        return cmdLine
    }

    override fun onUnityPlayerUnloaded() {
        moveTaskToBack(true)
    }

    override fun onUnityPlayerQuitted() {}

    override fun onNewIntent(intent: Intent?) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent)
        mUnityPlayer!!.newIntent(intent)
        super.onNewIntent(intent)
    }

    // Quit Unity
    override fun onDestroy() {

        mUnityPlayer!!.destroy()
        super.onDestroy()
    }

    // If the activity is in multi window mode or resizing the activity is allowed we will use
    // onStart/onStop (the visibility callbacks) to determine when to pause/resume.
    // Otherwise it will be done in onPause/onResume as Unity has done historically to preserve
    // existing behavior.
    override fun onStop() {
        super.onStop()
        Log.i("lldebug", "onstop")
        mUnityPlayer!!.pause()
    }

    override fun onStart() {
        super.onStart()
        mUnityPlayer!!.resume()
    }

    // Pause Unity
    override fun onPause() {
        IHEventBus.instance?.post(BusMsg(BusMsg.APP_EXIT,null))
        super.onPause()
        Log.i("lldebug", "onpause")


    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        mUnityPlayer!!.resume()
//        mUnityPlayer!!.requestFocus()
//        Log.i("lldebug delay","before")

        var timer=Timer()
        class MyTimerTask():TimerTask(){
            override fun run() {
                runOnUiThread {
                    Log.i("lldebug delay","after")
                    mUnityPlayer!!.requestFocus() }
            }
        }
        var timerTask=MyTimerTask()
        timer.schedule(timerTask,5000)
    }

    // Low Memory Unity
    override fun onLowMemory() {
        Log.i("lldebug onLowMemory", "onLowMemory")
        super.onLowMemory()
        mUnityPlayer!!.lowMemory()
    }

    // Trim Memory Unity

    override fun onTrimMemory(level: Int) {
        Log.i("lldebug onTrimMemory", "onTrimMemory")
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer!!.lowMemory()
        }
    }

    // This ensures the layout will be correct.
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mUnityPlayer!!.configurationChanged(newConfig)
    }

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        mUnityPlayer!!.windowFocusChanged(hasFocus)
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_MULTIPLE) mUnityPlayer!!.injectEvent(event) else super.dispatchKeyEvent(
            event
        )
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    /*API12*/
    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    @Subscribe
    fun onEventLoginOrOut(event: BusMsg<Any>) {
        when (event.code) {
            BusMsg.SIGN_IN -> {
                mViewModel.login(this@MainActivity)
            }

            BusMsg.SIGN_OUT -> {
                mViewModel.logout()
            }

            BusMsg.REFRESH_USER_INFO -> {
                mViewModel.getUserInfo()
            }

            BusMsg.PAY_FETCH_ALL_PACK_PRODUCT_LIST -> {
                mViewModel.getProductList()

            }

            BusMsg.PAY_FETCH_USER_STATUS -> {
                mViewModel.getUserStatus()

            }

            //old
            BusMsg.PAY_PURCHASE_ALL_PACK_WITH_PRODUCT_ID -> {
                mViewModel.createOrder(event.data as String)
            }

            //new

            BusMsg.PAY_PURCHASE_WITH_PRODUCT_ID -> {
                mViewModel.purchaseWithProductID(event.data as String)
            }

            BusMsg.PAY_RESTORE_PURCHASE -> {
                mViewModel.resumePurchase()
            }




        }
    }



    override fun mergeWithData(
        resultMap: MutableMap<Any?, Any?>?,
        resultString: String?,
        needForceFlush: Boolean,
    ) {
        IHEventBus.instance?.post(
            BusMsg(
                BusMsg.MONGO_MERGE_WITH_DATA,
                MogoData(resultMap, needForceFlush)
            )
        )
    }


}