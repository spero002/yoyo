###前言
yoyo是一个玩具项目，现在的目的是实现多种编程语言访问ucloud api的demo。所有代码的组织方式都是类似的，唯一不同的地方在于使用的编程语言的差异。其最终目的是为其他使用这个api的用户提供一个参考，同时因为这里的提供的代码都是验证过的，可以给大家编写自己代码的时候提供一个对照，这样便于排查自己在实际使用过程中遇到的问题。
###UCloud API简介
API是UCloud提供的一种用来访问其提供的服务的方法，可以对平台上的资源进行相关的操作。和控制台的方式相对应，API提供了一种在程序或者说使用脚本访问资源的方式。API的调用通过HTTP/HTTPS GET的方式来实现，一次调用的过程就是一次向服务器发起一个GET请求的过程，请求的结果是通过GET请求返回的结果实现的。返回的结果是JSON格式的，可以通过相应编程语言提供的相应方法获取感兴趣的信息来实现设计的功能。调用的方法具体来说，就是通过相应的函数构造请求url，在通过编程语言提供的GET方式来向服务器发起请求。请求的url的构造方式是在相应的API调用地址后面加上公共参数，API指令和指令参数，已经由平台提供的API密匙。由于API操作直接涉及平台上的资源，所以需要通过密匙来进行身份认证。注意妥善保管API密匙，这个是API访问认真的重要参数。
###请求结构
名字     | 描述             | 注意
-------- | ------           | ------
调用地址 | API请求的入口地址| http(s)://api.ucloud.cn
公共参数 | 机房信息等必要参数| 
API指令  | 具体请求的动作    |如DescribeUhostInstance
指令参数 | 可选参数，对请求进行限制|如申请主机时指定CPU数目

###请求示例
下面是一个获取主机信息的请求示例，此实例获取BGP-A机房，UHostID为uhost-qs20fr的主机的相关信息，注意这里的Signature并不是真实环境下的Signature，只用来示例而已，这只是一个示例，将此链接直接发送给服务器是无效的。
```
http(s)://api.ucloud.cn/?Action=DescribeUHostInstance
&Region=cn-north-01
&UHostIds.0=uhost-qs20fr
&Offset=0
&Limit=20
&Signature=2697152c34abbc148a38a33c0dc0d3d7b99ce82f
```
###返回结构
名字                     | 描述                        | 注意
----------               |----------                   |-------
指令名称                | 返回调用指令的名字           | 形式如"API指令名Responese"
返回码                  | 参数名未ret_code，此值为0时表示正常返回，非零表示出错|
返回参数                | 对返回结果的描述             | 和每个请求有关

###返回示例
下面是一个获取主机信息（DescribeUHostInstance）的API请求的返回，API请求返回的数据是通过JSON格式组织的。
```JSON
{
     "Action" : "DescribeUHostInstanceResponse",
     "TotalCount" : 1,
     "RetCode" : 0,
     "UHostSet" : [
         {
             "UHostId":"uhost-qs20fr",
             "ImageId": "ee51e7ba-7b48-4332-b6e8-990bd56f81af",
             "BasicImageId": "08d3b48b-ba14-4b1a-a5dd-cbacf8b820f2",
             "BasicImageName": "Ubuntu 10.04 64位",
             "Tag":"tag-test",
             "Name":"name-test",
             "Remark":"remark-test",
             "State":"Running",
             "CreateTime":1234567890,
             "ChargeType":"Dynamic",
             "ExpireTime":1398328902,
             "CPU":2,
             "Memory":2048,
             "AutoRenew":"Yes",
             "NetworkState":"Connected",
             "NetCapability":"Yes",
             "AutoRenew": "Yes",
             "DiskSet":[
                 {
                     "Type": "Boot",
                     "DIskId": "209883a7-aaee-4492-974d-5d9d64ef79c4",
                     "Drive": "/dev/sda",
                     "Size": 20
                 },
                 {
                     "Type": "Data",
                     "DIskId": "5daef46a-63e6-40c2-8d0a-d3d5b3bc4d5b",
                     "Drive": "/dev/sdb",
                     "Size": 40
                 },
                 {
                     "Type": "Udisk",
                     "DIskId": "ce8a9f2a-28c7-4267-8cff-eafb714e3b18",
                     "Drive": "/dev/sdc",
                     "Size": 100
                 }
             ]
             "IpSet":[
                 {
                     "Type": " Private ",
                     "IP":"10.6.6.1"
                 },
                 {
                     "Type":" Bgp ",
                     "IPId":"cc9732a6-d43a-45ca-b867-fb90052ffe88",
                     "IP":"118.192.72.21",
                     "Bandwidth": 2
                 }
             ]
         }
     ]
 }
```
###公共参数
公共参数是在操作所有 API 的时候，都必需给出的参数。其中ProjectId是在子项目功能被提出来后添加的参数，因此为了保证向后兼容，这个参数并不做强制要求，不指定的情况下这个值默认为默认项目。下面列表中列出了这4个必选参数

 参数名     | 必选 | 参数类型 | 说明
 --------   |----- |------    |------
 Action     | 是   |  String  | 对应API名称，如CreateUHostInstance，详细列表见官方文档
 PublicKey  | 是   | String   | 用户公钥，由UCloud平台提供，可以在控制台左侧的API密匙条目下查看
 Signature  |是    | String   | 根据参数和公钥生成，生成算法会在下面说明
 ProjectId  | 否   | String   | 项目ID，为空时及为默认项目
 
###签名算法
签名是加在请求链接后面有认证功能的一个字符串。签名的计算需要用到用户的PublicKey和PrivateKey。其中PublicKey当作普通参数一样处理，PrivateKey是正在进行has计算之前加在计算出的字符串后面的。这里假设用户的PublicKey和PrivateKey为如下值，这两个值可以在测试代码的时候用来检验算法的正确性，如果算法正确的计算出来的Signature值应该和这个文档中计算出来的是一致的。
```
PublicKey  = 'ucloudsomeone@example.com1296235120854146120'
PrivateKey = '46f09bb9fab4f12dfc160dae12273d5332b5debe'
```
对用户的请求做如下假设：
```
{
    "Action"     :  "CreateUHostInstance",
    "Region"     :  "cn-north-01",
    "ImageId"    :  "f43736e1-65a5-4bea-ad2e-8a46e18883c2",
    "CPU"        :  2,
    "Memory"     :  2048,
    "DiskSpace"  :  10,
    "LoginMode"  :  "Password",
    "Password"   :  "VUNsb3VkLmNu",
    "Name"       :  "Host01",
    "ChargeType" :  "Month",
    "Quantity"   :  1,
    "PublicKey"  :  "ucloudsomeone@example.com1296235120854146120"
}
```
详细的计算过程如下：
####1.将请求参数按照名进行升序排列
```
{
    "Action"     :  "CreateUHostInstance",
    "CPU"        :  2,
    "ChargeType" :  "Month",
    "DiskSpace"  :  10,
    "ImageId"    :  "f43736e1-65a5-4bea-ad2e-8a46e18883c2",
    "LoginMode"  :  "Password",
    "Memory"     :  2048,
    "Name"       :  "Host01",
    "Password"   :  "VUNsb3VkLmNu",
    "PublicKey"  :  "ucloudsomeone@example.com1296235120854146120",
    "Quantity"   :  1,
    "Region"     :  "cn-north-01"
}
```
####2.对排序后的请求参数进行URL编码
```
{
     "Action"     :  "CreateUHostInstance",
     "CPU"        :  2,
     "ChargeType" :  "Month",
     "DiskSpace"  :  10,
     "ImageId"    :  "f43736e1-65a5-4bea-ad2e-8a46e18883c2",
     "LoginMode"  :  "Password",
     "Memory"     :  2048,
     "Name"       :  "Host01",
     "Password"   :  "VUNsb3VkLmNu",
     "PublicKey"  :  "ucloudsomeone%40example.com1296235120854146120",
     "Quantity"   :  1,
     "Region"     :  "cn-north-01"
 }
```
####3.构造HTTP请求
参数名和参数值之间用 “=” 连接，参数和参数之间用”&”号连接，构造的URL请求为:
```
http(s)://api.ucloud.cn/?Action=CreateUHostInstance
&CPU=2
&ChargeType=Month
&DiskSpace=10
&ImageId=f43736e1-65a5-4bea-ad2e-8a46e18883c2
&LoginMode=Password
&Memory=2048
&Name=Host01
&Password=VUNsb3VkLmNu
&PublicKey=ucloudsomeone%40example.com1296235120854146120
&Quantity=1
&Region=cn-north-01
```
####4.构造被签名参数串
被签名串的构造规则为: 被签名串 = 所有请求参数拼接(无需HTTP转义)。并在本签名串的结尾拼接API密钥的私钥（PrivateKey）。
```
ActionCreateUHostInstanceCPU2ChargeTypeMonthDiskSpace10ImageIdf43736e1-65a5-4bea-ad2e-8a46e18883c2LoginModePasswordMemory2048NameHost01PasswordVUNsb3VkLmNuPublicKeyucloudsomeone@example.com1296235120854146120Quantity1Regioncn-north-0146f09bb9fab4f12dfc160dae12273d5332b5debe
```
####5.计算签名
生成被签名串的 SHA1 签名，即是请求参数”Signature”的值。
按照上述算法，本例中，计算出的Signature为
```
 64e0fe58642b75db052d50fd7380f79e6a0211bd 
```
####6.使用签名组合HTTP请求
将签名参数附在原有请求串的最后面。最终的HTTP请求串为(为了查看方便，我们人为地将参数之间用回车分隔开)
```
http(s)://api.ucloud.cn/?Action=CreateUHostInstance
&CPU=2
&ChargeType=Month
&DiskSpace=10
&ImageId=f43736e1-65a5-4bea-ad2e-8a46e18883c2
&LoginMode=Password
&Memory=2048
&Name=Host01
&Password=VUNsb3VkLmNu
&PublicKey=ucloudsomeone%40example.com1296235120854146120
&Quantity=1
&Region=cn-north-01
&Signature=64e0fe58642b75db052d50fd7380f79e6a0211bd
```

###签名示例代码
```python
import hashlib
import urlparse
import urllib

def _verfy_ac(private_key, params):
    items=params.items()
    # 请求参数串
    items.sort()
    # 将参数串排序

    params_data = "";
    for key, value in items:
        params_data = params_data + str(key) + str(value)
    params_data = params_data + private_key

    sign = hashlib.sha1()
    sign.update(params_data)
    signature = sign.hexdigest()

    return signature
    # 生成的Signature值
```
