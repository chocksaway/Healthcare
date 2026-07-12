package com.chocksaway.healthcare.testutils;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.dto.ActionDTO;

import java.time.Instant;

public class ActionTestBuilder {
    private Long id;
    private String activity = "ACT";
    private String context = "CTX";
    private String moduleId = "MODULE";
    private Instant whenRecorded = Instant.parse("2024-01-01T00:00:00Z");

    public static ActionTestBuilder anAction() {
        return new ActionTestBuilder();
    }

    public ActionTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ActionTestBuilder withActivity(String activity) {
        this.activity = activity;
        return this;
    }

    public ActionTestBuilder withContext(String context) {
        this.context = context;
        return this;
    }

    public ActionTestBuilder withModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }

    public ActionTestBuilder withWhenRecorded(Instant whenRecorded) {
        this.whenRecorded = whenRecorded;
        return this;
    }

    public Action build() {
        Action a = new Action();
        if (id != null) a.setId(id);
        a.setActivity(activity);
        a.setContext(context);
        a.setModuleId(moduleId);
        a.setWhenRecorded(whenRecorded);
        return a;
    }

    public ActionDTO buildDTO() {
        ActionDTO dto = new ActionDTO();
        if (id != null) dto.setId(id);
        dto.setActivity(activity);
        dto.setContext(context);
        dto.setModuleId(moduleId);
        dto.setWhenRecorded(whenRecorded);
        return dto;
    }
}

