package org.newtonproject.newtoncore.android.data.repository;

import com.google.gson.Gson;

import org.newtonproject.newtoncore.android.data.entity.common.TransactionNotification;
import org.newtonproject.newtoncore.android.data.repository.entity.RealmDappInfo;
import org.newtonproject.newtoncore.android.data.repository.entity.RealmNotificationInfo;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class RealmDatabaseMigration implements RealmMigration {
    private static final String TAG = "RealmDatabase";
    private Gson gson = new Gson();

    @Override
    public int hashCode() {
        return RealmDatabaseMigration.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        return obj instanceof RealmDatabaseMigration;
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema( );

        if(oldVersion == 0) {
            schema.remove(RealmDappInfo.class.getSimpleName());
            RealmObjectSchema dappRealm = schema.createWithPrimaryKeyField(RealmDappInfo.class.getSimpleName(), "bundleId", String.class);
            dappRealm.addField("dAppId", String.class);
            dappRealm.addField("dAppName", String.class);
            dappRealm.addField("dappUrl", String.class);
            dappRealm.addField("dappImage", String.class);
            dappRealm.addField("dappVersion", String.class);
            dappRealm.addField("dappDesc", String.class);
            dappRealm.addField("dappDownloadUrl", String.class);
            dappRealm.addField("dappOwner", String.class);
            dappRealm.addField("dappStatus", int.class);
            dappRealm.addField("type", int.class);
            dappRealm.addField("md5", String.class);
            dappRealm.addField("openType", int.class);
            oldVersion++;
        }

        if (oldVersion == 2) {
            RealmObjectSchema realmObjectSchema = schema.get(RealmNotificationInfo.class.getSimpleName());
            realmObjectSchema.removePrimaryKey();
            realmObjectSchema.addField("message_id", String.class);
            realmObjectSchema.addField("send_time", long.class);
            realmObjectSchema.addField("message_type", int.class);
            realmObjectSchema.addField("title", String.class);
            realmObjectSchema.addField("body", String.class);
            realmObjectSchema.addField("payload", String.class);
            realmObjectSchema.addField("language", String.class);
            realmObjectSchema.transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject obj) {
                    TransactionNotification info = new TransactionNotification();
                    info.from_address = obj.get("from_address");
                    info.txid = obj.get("txid");
                    info.blockheight = obj.get("blockheight");
                    info.to_address = obj.get("to_address");
                    info.value = obj.get("value");
                    info.time = obj.get("time");
                    info.msg_type = obj.get("msg_type");
                    obj.set("send_time", System.currentTimeMillis());
                    obj.set("message_type", info.msg_type);
                    obj.set("title", "transaction record");
                    obj.set("body", "transaction body");
                    obj.set("payload", gson.toJson(info));
                    obj.set("language", "en");
                    obj.set("message_id", info.txid);
                }
            });
            realmObjectSchema.addIndex("send_time");
            realmObjectSchema.addIndex("txid");
            realmObjectSchema.addPrimaryKey("message_id");
            realmObjectSchema.removeField("msg_type");
            realmObjectSchema.removeField("blockheight");
            realmObjectSchema.removeField("from_address");
            realmObjectSchema.removeField("to_address");
            realmObjectSchema.removeField("value");
            realmObjectSchema.removeField("time");
            oldVersion++;
        }
    }
}
