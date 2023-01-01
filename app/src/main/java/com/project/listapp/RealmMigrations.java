package com.project.listapp;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            final RealmSchema schema = realm.getSchema();

            if (oldVersion == 1) {
                final RealmObjectSchema userSchema = schema.get("Tasks");
                userSchema.addField("userID", String.class);
                userSchema.addField("dateCreated", String.class);
                userSchema.addField("dueDate", String.class);
            }
        }

    public int hashCode() {
        return RealmMigrations.class.hashCode();
    }

    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }
        return object instanceof RealmMigrations;
    }


}
