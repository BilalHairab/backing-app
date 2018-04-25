package com.bilal.backing.interfaces;

import java.io.Serializable;

public interface OnStepChanged extends Serializable{
    void onNextStep();
    void onChangeStep(int position);
}
