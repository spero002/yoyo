#!/bin/bash

source config.sh;

# 生成signature的函数
function _verfy_ac() {
    declare -A $params;
    sort $params;
    $params_data="";

    for key in ${!params[*]}
    do
        $params_data="$params_data""$key";
        $params_data="$params_data""${!params[$key]}";
    done
    
    $params_data="$params_data""$private_key";
    return sha1sum $params_data;
}

# 用于发起GET请求获取数据
function get() {

}

# 实际完成对外封装的访问函数
function apiclient() {

}
