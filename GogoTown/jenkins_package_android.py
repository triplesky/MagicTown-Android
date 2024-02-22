# coding=utf-8
# @Author: zhenwei
# @Date:   2019-04-29 11:22:41
# @Last Modified by:   zhenwei
# @Last Modified time: 2019-07-24 15:26:49

import sys
import os
import shutil
import getopt
import shutil
import platform

PRODUCT_FLAVORS = ""
BUILD_TYPE = ""
CHANNELS = ""
IS_JIAGU = 'true'

APP_MODULE_NAME = "app"
KEY_STORE_PATH = ""
KEY_STORE_PSW = ""
KEY_STORE_ALIAS = ""
KEY_STORE_ALIAS_PSW = ""

CHANNEL_JSON_PATH = ""
IS_OUTPUT_APK = 'false'

PROJECT_ROOT_PATH = os.getcwd()
#/apks/
APK_ROOT_DIR = PROJECT_ROOT_PATH + os.path.sep + "apks" + os.path.sep
APK_ORIGIN_DIR = APK_ROOT_DIR + "origin" + os.path.sep

# c++缓存
CXX_ROOT_DIR = PROJECT_ROOT_PATH + os.path.sep + "app" + os.path.sep + ".cxx" + os.path.sep

JIAGU_360_USER_NAME = "15232138798"
JIAGU_360_PASSWORD = "test258369"


APK_SIGNER_JAR_PATH = "/Users/edubuild7/Documents/workspace/workspace/walle/sign/apksigner-wang.jar"

ZIPALIGN_PATH = "D:/tools/Android/android-sdk-windows/build-tools/28.0.2/zipalign.exe"
JIAGU_PATH = "D:/workspace/workspace/jiagu_sign/360jiagubao_windows_64/jiagu"
if platform.system() != "Windows":
    JIAGU_PATH = "/Users/edubuild7/Documents/workspace/workspace/360jiagu/jiagu"
    ZIPALIGN_PATH = "/Users/edubuild7/Library/Android/sdk/build-tools/30.0.2/zipalign"


JIAGU_JAVA_PATH = JIAGU_PATH + "/java/bin/java"
JIAGU_JAR_PATH = JIAGU_PATH + "/jiagu.jar"

JAR_SIGNER = "jarsigner"

# 签名jar路径
APKSIGNER_PATH = "D:/workspace/workspace/jiagu_sign/walle/apksigner-wang.jar"

# 美团渠道打包
WALLE_JAR_PATH = "/Users/edubuild7/Documents/workspace/workspace/walle/sign/walle-cli-all.jar"
# CHANNEL_JSON_FILE_PATH = PROJECT_ROOT_PATH + os.path.sep + "channel/channel_config.json"

def main(arg):
    try:
        opts, args = getopt.getopt(arg, "hf:t:n:i:", ["flavor=", "buildtype=","appmodule=","isoutputapk="])
    except getopt.GetoptError:

        print( 'prepare_buildin_res_android.py -e <environment> -t <buildtype> -c <channel> -j <jiagu> -n <app_module_name>')
        sys.exit(-1)

    global PRODUCT_FLAVORS, BUILD_TYPE, CHANNELS, IS_JIAGU, KEY_STORE_PATH, KEY_STORE_PSW, KEY_STORE_ALIAS, KEY_STORE_ALIAS_PSW, APP_MODULE_NAME, CHANNEL_JSON_PATH, IS_OUTPUT_APK, BUILD_NUMBER, ABIFILTERS, MARKET, CMDPREFIX

    for opt, arg in opts:
        if opt in ('-h', '--help'):
                print('test.py')
                sys.exit()
        elif opt in ('-f', '--flavor'):
                PRODUCT_FLAVORS = arg.capitalize()
        elif opt in ('-t', '--buildtype'):
                BUILD_TYPE = arg.capitalize()
        elif opt in ('-n', '--appmodule'):
                APP_MODULE_NAME = arg
        elif opt in ('-w', '--channeljson'):
                CHANNEL_JSON_PATH = arg
        elif opt in ('-i', '--isoutputapk'):
                IS_OUTPUT_APK = arg

    KEY_STORE_PATH = PROJECT_ROOT_PATH + os.path.sep +  "keystore.jks"
    KEY_STORE_PSW = "gogotown"
    KEY_STORE_ALIAS = "gogotown"
    KEY_STORE_ALIAS_PSW = "gogotown"

    print ('PRODUCT_FLAVORS=', PRODUCT_FLAVORS)
    print ('BUILD_TYPE=', BUILD_TYPE)



    if PRODUCT_FLAVORS == '' or BUILD_TYPE == '':
    	print ('flavor or buildtype is nil')
    	sys.exit()

    if KEY_STORE_PATH =='' or KEY_STORE_PSW == '' or KEY_STORE_ALIAS == '' or KEY_STORE_ALIAS_PSW == '':
        print ('keystore = ' + KEY_STORE_PATH + "   psw=" +  KEY_STORE_PSW + "  alias=" + KEY_STORE_ALIAS + "  aliaspsw=" + KEY_STORE_ALIAS_PSW)
        sys.exit()

    init()

    javaVersionCommand = "java -version"
    os.system(javaVersionCommand)

    exeGradleCommand(IS_OUTPUT_APK)


    archiveName, archiveFullPath = findApkOrAabInOutput(IS_OUTPUT_APK)
    if archiveName == "" or archiveFullPath == "":
        print("[ERROR] not find apk in build output path!!!", archiveName, archiveFullPath)
        exit(-1)
    # 从build文件中将apk拷贝到该渠道的文件夹内
    shutil.copy(archiveFullPath, APK_ROOT_DIR)

def init():
    print ("ROOT_PATH_PY=", APK_ROOT_DIR)
    #初始化apks根目录
    if os.path.exists(CXX_ROOT_DIR):
        shutil.rmtree(CXX_ROOT_DIR)
    if os.path.exists(APK_ROOT_DIR):
       shutil.rmtree(APK_ROOT_DIR)
    os.makedirs(APK_ROOT_DIR)
    if APK_ROOT_DIR == "" or not os.path.exists(APK_ROOT_DIR):
        print ("[ERROR] APK_ROOT_DIR is null or APK_ROOT_DIR is not exists")


def exeGradleCommand(is_output_apk):
    if not isWindows():
        changeModeCommand = "chmod +x gradlew"
        os.system(changeModeCommand)

    if len(APP_MODULE_NAME) > 0:
        appDir = PROJECT_ROOT_PATH + os.path.sep + APP_MODULE_NAME
        os.chdir(appDir)
    print("PRODUCT_FLAVORS=" + PRODUCT_FLAVORS + "  BUILD_TYPE=" + BUILD_TYPE  + " is_output_apk=" + is_output_apk)
    gradleCleanCommand = PROJECT_ROOT_PATH + os.path.sep + "gradlew clean"

    print
    "gradleCleanCommand", gradleCleanCommand
    os.system(gradleCleanCommand)

    if is_output_apk == "true":
        gradlePackCommand = PROJECT_ROOT_PATH + os.path.sep + "gradlew assemble" + PRODUCT_FLAVORS + BUILD_TYPE
    else:
        gradlePackCommand = PROJECT_ROOT_PATH + os.path.sep + "gradlew :app:bundle" + PRODUCT_FLAVORS + BUILD_TYPE

    print
    "gradlePackCommand", gradlePackCommand
    os.system(gradlePackCommand)



# 查找build中的apk及mapping文件，并返回[1]apk文件名字、[2]apk路径、[3]mapping文件路径
def findApkOrAabInOutput(is_output_apk):
    print("PROJECT_ROOT_PATH=", PROJECT_ROOT_PATH)

    apkDirName = "apk"
    if is_output_apk == "false":
        apkDirName = "bundle"

    flavorDirName = decapitalize(PRODUCT_FLAVORS) + os.path.sep + BUILD_TYPE.lower()
    if is_output_apk == "false":
        flavorDirName = decapitalize(PRODUCT_FLAVORS) + BUILD_TYPE

    appModuleDir = PROJECT_ROOT_PATH
    if len(APP_MODULE_NAME) > 0:
        appModuleDir = PROJECT_ROOT_PATH + os.path.sep + APP_MODULE_NAME
    apkDir = appModuleDir + os.path.sep + "build" + os.path.sep + "outputs" + os.path.sep + apkDirName + os.path.sep + flavorDirName + os.path.sep

    print("apkDir=", apkDir)
    if not os.path.exists(apkDir):
        print("apk dir is empty ", PRODUCT_FLAVORS, BUILD_TYPE)
        sys.exit(-1)

    files = os.listdir(apkDir)
    if len(files) == 0:
        print("apk dir is empty ", PRODUCT_FLAVORS, BUILD_TYPE)
        sys.exit(-1)

    for fileName in files:
        print("fileName=", fileName)
        if fileName.endswith(".apk") or fileName.endswith(".aab"):
            return fileName, apkDir + fileName

    return "", ""


def decapitalize(string):
    return string[:1].lower() + string[1:]




def isWindows():
    print( "platform.system=", platform.system())
    if platform.system() == "Windows":
        return True
    return False


if __name__ == '__main__':
    main(sys.argv[1:])