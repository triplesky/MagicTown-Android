package com.bekids.gogotown.unity.bridge.log;

import android.text.TextUtils;

import com.bekids.gogotown.unity.bridge.dispatcher.UnityMessage;
import com.bekids.gogotown.util.string.CharacterFormat;

import timber.log.Timber;

/**
 * Author: LuckyFind
 * Date: 2021/3/30
 * Desc:
 */
public class UnityBridgeLogPrinter implements IFormatPrinter {
    private static final String TAG = "UnityBridge";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

    private static final String N = "\n";
    private static final String T = "\t";
    private static final String REQUEST_UP_LINE = "   ┌────── UnityRequest  ";
    private static final String END_LINE = "   └───────────────────────────────────────────────────────────────────────";
    private static final String RESPONSE_UP_LINE = "   ┌────── CallUnity ──────────────────────────────────────────────";
    private static final String PROCESS_METHOD_TAG = "ProcessMethod:";
    private static final String PARAMS_TAG = "Params: ";
    private static final String RETURN_TAG = "ReturnValue: ";
    private static final String BLOCKID_TAG = "BlockId:";
    private static final String STRATEGY_TAG = "TargetStrategy:";
    private static final String ERROR_TAG = "ErrorInfo:";
    private static final String CALLBACK_METHOD_TAG = "CallbackMethod:";
    private static final String CORNER_UP = "┌ ";
    private static final String CORNER_BOTTOM = "└ ";
    private static final String CENTER_LINE = "├ ";
    private static final String DEFAULT_LINE = "│ ";
    private static final String ON_MESSAGE_TAG = "OnMessage:";
    private static volatile UnityBridgeLogPrinter instance;

    private UnityBridgeLogPrinter() {
    }

    public static UnityBridgeLogPrinter getInstance() {
        if (instance == null) {
            synchronized (UnityBridgeLogPrinter.class) {
                if (instance == null) {
                    instance = new UnityBridgeLogPrinter();
                }
            }
        }
        return instance;
    }

    private static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || N.equals(line) || T.equals(line) || TextUtils.isEmpty(line.trim());
    }

    @Override
    public void printUnityRawRequestInfo(String methodName, String params, String blockID) {
        final String tag = getTag(0);
        Timber.tag(tag).i(getUpLineTag(0));

        final String strParams = LINE_SEPARATOR + PARAMS_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(params);
        logLinesInfo(tag, new String[]{PROCESS_METHOD_TAG + methodName}, false);
        logLinesInfo(tag,strParams.split(LINE_SEPARATOR), true);
        Timber.tag(resolveTag(tag)).i(DEFAULT_LINE );
        logLinesInfo(tag, new String[]{BLOCKID_TAG + blockID}, false);
        Timber.tag(tag).i(END_LINE);
    }

    @Override
    public void printDispatchUnityMessageInfo(UnityMessage unityMessage, String targetStrategy) {
        final String tag = getTag(1);
        Timber.tag(tag).i(getUpLineTag(1));
        final String strParams = LINE_SEPARATOR + PARAMS_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(unityMessage.getParams());
        logLinesInfo(tag, new String[]{PROCESS_METHOD_TAG + unityMessage.getMethodName()}, false);
        logLinesInfo(tag, strParams.split(LINE_SEPARATOR), true);
        Timber.tag(resolveTag(tag)).i(DEFAULT_LINE );
        logLinesInfo(tag, new String[]{BLOCKID_TAG + unityMessage.getBlockid()}, false);
        logLinesInfo(tag, new String[]{STRATEGY_TAG + targetStrategy}, false);
        Timber.tag(tag).i(END_LINE);
    }

    @Override
    public void printDispatchUnityMessageError(UnityMessage unityMessage, String err) {
        final String tag = getTag(2);
        Timber.tag(tag).e(getUpLineTag(2));

        final String strParams = LINE_SEPARATOR + PARAMS_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(unityMessage.getParams());
        logLinesError(tag, new String[]{PROCESS_METHOD_TAG + unityMessage.getMethodName()}, false);
        logLinesError(tag,strParams.split(LINE_SEPARATOR), true);
        Timber.tag(resolveTag(tag)).e(DEFAULT_LINE );
        logLinesError(tag, new String[]{BLOCKID_TAG + unityMessage.getBlockid()}, false);
        logLinesError(tag, new String[]{ERROR_TAG + err}, false);
        Timber.tag(tag).e(END_LINE);
    }

    @Override
    public void printProcessUnityMessageInfo(String strategyName, String methodName, String params, String blockID,String returnValue) {
        final String tag = getTag(3);
        final String strParams = LINE_SEPARATOR + PARAMS_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(params);
        final String returnStrParams = LINE_SEPARATOR + RETURN_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(returnValue);
        Timber.tag(tag).d(getUpLineTag(3));
        logLinesDebug(tag, new String[]{STRATEGY_TAG + strategyName}, false);
        logLinesDebug(tag, new String[]{PROCESS_METHOD_TAG + methodName}, false);
        logLinesDebug(tag,  strParams.split(LINE_SEPARATOR), true);
        Timber.tag(resolveTag(tag)).d(DEFAULT_LINE );
        logLinesDebug(tag, new String[]{BLOCKID_TAG + blockID}, false);
        logLinesDebug(tag,  returnStrParams.split(LINE_SEPARATOR), true);
        Timber.tag(tag).d(END_LINE);
    }

    @Override
    public void printProcessUnityMessageError(String strategyName, String methodName, String params, String blockID, String err) {
        final String tag = getTag(4);
        final String strParams = LINE_SEPARATOR + PARAMS_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(params);
        Timber.tag(tag).e(getUpLineTag(4));
        logLinesError(tag, new String[]{STRATEGY_TAG + strategyName}, false);
        logLinesError(tag, new String[]{PROCESS_METHOD_TAG + methodName}, false);
        logLinesError(tag, strParams.split(LINE_SEPARATOR), true);
        logLinesError(tag, new String[]{BLOCKID_TAG + blockID}, false);
        logLinesError(tag, new String[]{ERROR_TAG + err}, false);
        Timber.tag(tag).e(END_LINE);
    }

    @Override
    public void printUnityMessageCallbackInfo(String strategyName, String method, String params, String blockId, String originMethod) {
        if("engine_did_recive_sound".equals(method)){
            return;
        }
        final String tag = getTag(5);
        final String strParams = LINE_SEPARATOR + PARAMS_TAG + LINE_SEPARATOR + CharacterFormat.jsonFormat(params);
        Timber.tag(tag).d(RESPONSE_UP_LINE);
        logLinesDebug(tag, new String[]{STRATEGY_TAG + strategyName}, false);
        logLinesDebug(tag, new String[]{CALLBACK_METHOD_TAG + method}, false);
        logLinesDebug(tag, new String[]{PROCESS_METHOD_TAG + originMethod}, false);
        logLinesDebug(tag, strParams.split(LINE_SEPARATOR), true);
        Timber.tag(resolveTag(tag)).d(DEFAULT_LINE );
        logLinesDebug(tag, new String[]{BLOCKID_TAG + blockId}, false);
        Timber.tag(tag).d(END_LINE);
    }


    /**
     * 对 {@code lines} 中的信息进行逐行打印
     *
     * @param tag
     * @param lines
     * @param withLineSize 为 {@code true} 时, 每行的信息长度不会超过250 超过则自动换行
     */
    private static void logLinesInfo(String tag, String[] lines, boolean withLineSize) {
        for (String line : lines) {
            int lineLength = line.length();
            int MAX_LONG_SIZE = withLineSize ? 250: lineLength==0?150:lineLength;
            for (int i = 0; i <= lineLength / MAX_LONG_SIZE; i++) {
                int start = i * MAX_LONG_SIZE;
                int end = (i + 1) * MAX_LONG_SIZE;
                end = end > line.length() ? line.length() : end;
                Timber.tag(resolveTag(tag)).i(DEFAULT_LINE + line.substring(start, end));
            }
        }
    }

    private static void logLinesDebug(String tag, String[] lines, boolean withLineSize) {
        for (String line : lines) {
            int lineLength = line.length();
            int MAX_LONG_SIZE = withLineSize ? 250 : lineLength==0?150:lineLength;
            for (int i = 0; i <= lineLength / MAX_LONG_SIZE; i++) {
                int start = i * MAX_LONG_SIZE;
                int end = (i + 1) * MAX_LONG_SIZE;
                end = end > line.length() ? line.length() : end;
                Timber.tag(resolveTag(tag)).d(DEFAULT_LINE + line.substring(start, end));
            }
        }
    }

    private static void logLinesError(String tag, String[] lines, boolean withLineSize) {
        for (String line : lines) {
            int lineLength = line.length();
            int MAX_LONG_SIZE = withLineSize ? 250 : lineLength==0?150:lineLength;
            for (int i = 0; i <= lineLength / MAX_LONG_SIZE; i++) {
                int start = i * MAX_LONG_SIZE;
                int end = (i + 1) * MAX_LONG_SIZE;
                end = end > line.length() ? line.length() : end;
                Timber.tag(resolveTag(tag)).e(DEFAULT_LINE + line.substring(start, end));
            }
        }
    }

    private static String resolveTag(String tag) {
        return computeKey() + tag;
    }

    private static ThreadLocal<Integer> last = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private static final String[] ARMS = new String[]{"-A-", "-R-", "-M-", "-S-"};

    private static String computeKey() {
        if (last.get() >= 4) {
            last.set(0);
        }
        String s = ARMS[last.get()];
        last.set(last.get() + 1);
        return s;
    }


    private static String getTag(int type) {
        if (type == 0) {
            return TAG + "-OnMessage";
        } else if (type == 1) {
            return TAG + "-DispatchMessage";
        } else if (type == 2) {
            return TAG + "-DispatchMessageError";
        } else if (type == 3) {
            return TAG + "-HandleMessage";
        } else if (type ==4){
            return TAG + "-HandleMessageError";
        }else {
            return TAG + "-CallUnity";
        }

    }

    private static String getUpLineTag(int type){
        if (type == 0) {
            return REQUEST_UP_LINE + "OnMessage───────────────────────────────────────────────";
        } else if (type == 1) {
            return REQUEST_UP_LINE + "DispatchMessage─────────────────────────────────────────";
        } else if (type == 2) {
            return REQUEST_UP_LINE + "DispatchMessageError────────────────────────────────────";
        } else if (type == 3) {
            return REQUEST_UP_LINE + "HandleMessage───────────────────────────────────────────";
        } else {
            return REQUEST_UP_LINE + "HandleMessageError──────────────────────────────────────";
        }
    }
}
