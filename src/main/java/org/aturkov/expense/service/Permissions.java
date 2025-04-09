package org.aturkov.expense.service;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum Permissions {
    SYSTEM_ITEM(UUID.fromString("00000000-0000-0000-0001-000000000001"), UUID.fromString("00000000-0000-0000-0005-000000000001"));
    private final UUID id;
    private final UUID permissionGroupId;

    Permissions(UUID id, UUID permissionGroupId) {
        this.id = id;
        this.permissionGroupId = permissionGroupId;
    }

}
