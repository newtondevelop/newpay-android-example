## 环境配置

1. Android Studio 3.5
2. MinSdkVersion: 21
3. TargetSdkVersion: 28

## 构建说明

1. Android Studio 打开已存在的项目-> 项目根为 ../src 文件夹, 直接同步即可。

## 基础钱包功能

- [创建助记词](src/app/src/main/java/org/newtonproject/newtoncore/android/data/service/NewKeystoreAccountService.java#L66)

- [导入助记词](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L82)

- [导入Keystore](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L102)

- [导入私钥](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L59)

- [导出Keysotre](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L175)

- [导出助记词](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L189)

- [导出私钥](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/crypto/NewtonKeystore.java#L203)

- [地址格式](src/app/src/main/java/org/newtonproject/newtoncore/android/utils/NewAddressUtils.java)

## 依赖项说明

```
// 支持 bip44 协议
implementation 'org.newtondeveloper:newpay-keystore:0.0.1'
```

```
// newton web3j core
implementation 'org.newtonproject.web3j:core:4.1.7-android'
```
