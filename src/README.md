## 环境配置

1. Android Studio 3.2.2
2. MinSdkVersion: 21

## 构建说明

1. Android Studio 打开已存在的项目-> 项目根为 ../src 文件夹, 直接同步即可。

## 基础钱包功能

- [创建助记词](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/data/service/NewKeystoreAccountService.java#L66)

- [导入助记词](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L82)

- [导入Keystore](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L102)

- [导入私钥](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L59)

- [导出Keysotre](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L175)

- [导出助记词](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L189)

- [导出私钥](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L203)

- [地址格式](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/app/src/main/java/org/newtonproject/newtoncore/android/utils/NewAddressUtils.java)

- [创建助记词与BTC有区别的地方](https://gitlab.newtonproject.org/weixuefeng/newton-core/blob/master/newpay-android-example/src/core/src/main/java/org/bitcoinj/crypto/HDKeyDerivation.java#L66)

## 使用依赖 jar 说明

- abi-3.3.1-android、core-3.3.1-android、crypto-3.3.1-android、rlp-3.3.1-android、tuples-3.3.1-android、utils-3.3.1-android 为 newchain-web3j 构建，发送交易和调用合约的核心lib。其余为公共三方lib,也可使用其他依赖方式(gradle 等)