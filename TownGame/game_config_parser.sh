#!/bin/bash

# Usage:

# game_config_parser.sh \
# GAME_ID="town_pizzashop" \
# IS_IOS=1


# Output:

# CSV_GAME_ID: game_id
# CSV_SIMPLE_GAME_ID: simple_game_id
# CSV_DISPLAY_NAME: display_name
# CSV_BUNDLE_NAME: bundle_name
# CSV_APP_ID: app_id
# CSV_APPLE_ID: apple_id
# CSV_BUGLY_STAGING: 根据入参IS_IOS，返回对应平台的bugly_staging
# CSV_BUGLY: 根据入参IS_IOS，返回对应平台的bugly

main () {

    for ARGUMENT in "$@"
	do
		KEY=$(echo $ARGUMENT | cut -f1 -d=)
		VALUE=$(echo $ARGUMENT | cut -f2 -d=)

		case "$KEY" in
			GAME_ID)				GAME_ID=${VALUE};;
			IS_IOS)				    IS_IOS=${VALUE};;
			*)
		esac
	done

    if [ -z $GAME_ID ]; then
        printErrAndExit "入参GAME_ID不存在"
    fi

    if [ -z $IS_IOS ]; then
        IS_IOS=1
    fi

    local csv_path="game_config.csv"

    # local csv_line=$(awk -F',' '$1=="town_pizzashop" {print $0}' $csv_path)
    local csv_line=$(awk -F',' -v GAME_ID=${GAME_ID} '$1==GAME_ID {print $0}' $csv_path)

    # echo $csv_line

    IFS=',' read -r CSV_GAME_ID CSV_SIMPLE_GAME_ID CSV_DISPLAY_NAME CSV_BUNDLE_NAME CSV_APP_ID CSV_APPLE_ID __CSV_BUGLY_IOS_STAGING __CSV_BUGLY_IOS __CSV_BUGLY_ANDROID_STAGING __CSV_BUGLY_ANDROID CSV_PACKAGE_NAME CSV_GOOGLE_KEY <<< "$csv_line"

    trim_all_csv_variables

    check_csv_valid
}

trim_all_csv_variables () {
    # "$CSV_GAME_ID"不能省掉""，否则不安全，假如变量值包含空格，容易被当成多个参数。。
    # 不带""用$CSV_GAME_ID 或 ${CSV_GAME_ID}都不行，必须带""
    CSV_GAME_ID=$(trim_string "$CSV_GAME_ID")
    CSV_SIMPLE_GAME_ID=$(trim_string "$CSV_SIMPLE_GAME_ID")
    CSV_DISPLAY_NAME=$(trim_string "$CSV_DISPLAY_NAME")
    CSV_BUNDLE_NAME=$(trim_string "$CSV_BUNDLE_NAME")
    CSV_APP_ID=$(trim_string "$CSV_APP_ID")
    CSV_APPLE_ID=$(trim_string "$CSV_APPLE_ID")
    __CSV_BUGLY_IOS_STAGING=$(trim_string "$__CSV_BUGLY_IOS_STAGING")
    __CSV_BUGLY_IOS=$(trim_string "$__CSV_BUGLY_IOS")
    __CSV_BUGLY_ANDROID_STAGING=$(trim_string "$__CSV_BUGLY_ANDROID_STAGING")
    __CSV_BUGLY_ANDROID=$(trim_string "$__CSV_BUGLY_ANDROID")
    CSV_PACKAGE_NAME=$(trim_string "$CSV_PACKAGE_NAME")
     CSV_GOOGLE_KEY=$(trim_string "$CSV_GOOGLE_KEY")


}

is_first_apps () {
    # echo "is_first_apps: $GAME_ID"

    # bash 4.0才支持关联数组
    if [[ $GAME_ID == "town_car" || $GAME_ID == "town_happyhospital" || $GAME_ID == "town_pizzashop" ]]; then
        return 0
    else
        return 1
    fi
}

check_csv_valid () {

    if [[ -z $CSV_GAME_ID ]]; then
        printErrAndExit "game_id 没填"
    fi

    if [[ -z $CSV_SIMPLE_GAME_ID ]]; then
        # 注：这里${}可以，$()不行
        # ${}用于对变量进行特殊处理
        # $()用于执行命令并将命令的输出座位变量的值返回
        CSV_SIMPLE_GAME_ID=${CSV_GAME_ID/town_/}
        if [[ -z $CSV_SIMPLE_GAME_ID ]]; then
            printErrAndExit "simple_game_id 没填"
        fi
    fi

    if [[ -z $CSV_DISPLAY_NAME ]]; then
        printErrAndExit "display_name 没填"
    fi

    if [[ -z $CSV_BUNDLE_NAME ]]; then
        CSV_BUNDLE_NAME=$CSV_DISPLAY_NAME
        if [[ -z $CSV_BUNDLE_NAME ]]; then
            printErrAndExit "bundle_name 没填"
        fi
    fi

    if [[ -z $CSV_APP_ID ]]; then
        printErrAndExit "app_id 没填"
    fi

    if [[ -z $CSV_APPLE_ID ]]; then
        printErrAndExit "apple_id 没填"
    fi

    if [[ $IS_IOS -eq 1 ]]; then
        if [[ -z $__CSV_BUGLY_IOS ]]; then
            printErrAndExit "bugly_ios 没填"
        fi

        CSV_BUGLY=$__CSV_BUGLY_IOS
    else
        if [[ -z $__CSV_BUGLY_ANDROID ]]; then
            printErrAndExit "bugly_android 没填"
        fi

        CSV_BUGLY=$__CSV_BUGLY_ANDROID
    fi

    is_first_apps
    if [[ $? == 0 ]]; then
        echo "是第一批app"

        if [[ $IS_IOS -eq 1 ]]; then
            if [[ -z $__CSV_BUGLY_IOS_STAGING ]]; then
                printErrAndExit "bugly_ios_staging 没填"
            fi

            CSV_BUGLY_STAGING=$__CSV_BUGLY_IOS_STAGING
        else
            if [[ -z $__CSV_BUGLY_ANDROID_STAGING ]]; then
                printErrAndExit "bugly_android_staging 没填"
            fi

            CSV_BUGLY_STAGING=$__CSV_BUGLY_ANDROID_STAGING
        fi
    else
        # 其余拆包，bugly staging也往线上报
        if [[ $IS_IOS -eq 1 ]]; then
            if [[ -z $__CSV_BUGLY_IOS_STAGING ]]; then
                CSV_BUGLY_STAGING=$CSV_BUGLY
            else
                CSV_BUGLY_STAGING=$__CSV_BUGLY_IOS_STAGING
            fi

        else
            if [[ -z $__CSV_BUGLY_ANDROID_STAGING ]]; then
                CSV_BUGLY_STAGING=$CSV_BUGLY
            else
                CSV_BUGLY_STAGING=$__CSV_BUGLY_ANDROID_STAGING
            fi
        fi
    fi
}

trim_string () {
    local original_string=$1
    local trimmed_string="$(echo -e "${original_string}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
    echo "$trimmed_string"
}

printErr () {
	local msg=$1
	echo -e "\033[31m$(date "+%Y-%m-%d %H:%M:%S") [!] $msg\033[0m"
}

printErrAndExit () {
	printErr "game_config_parser解析失败: $1" && exit -1
}


main "$@"