package com.bridgelabz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class QuantityRequest {

    @NotNull
    @Valid
    private QuantityDTO q1;

    @NotNull
    @Valid
    private QuantityDTO q2;

    public QuantityDTO getQ1() {
        return q1;
    }

    public void setQ1(QuantityDTO q1) {
        this.q1 = q1;
    }

    public QuantityDTO getQ2() {
        return q2;
    }

    public void setQ2(QuantityDTO q2) {
        this.q2 = q2;
    }
}