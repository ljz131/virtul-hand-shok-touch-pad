package com.example.ljz131.myapplication;

/**
 * Created by ljz131 on 2017/5/6.
 */

public class TouchPadDataPackge {
    public int set=0;
    public int mode=3;

    public int slide=0;

    public int detax=0;
    public int detay=0;

    public int left_click=0;
    public int right_click=0;
    public int ctrl=0;

    public void init(){
        set=0;
        mode=3;
        slide=0;
        detax=0;
        detay=0;
        left_click=0;
        right_click=0;
        ctrl=0;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSlide() {
        return slide;
    }

    public void setSlide(int slide) {
        this.slide = slide;
    }

    public int getDetax() {
        return detax;
    }

    public void setDetax(int detax) {
        this.detax = detax;
    }

    public int getDetay() {
        return detay;
    }

    public void setDetay(int detay) {
        this.detay = detay;
    }

    public int getLeft_click() {
        return left_click;
    }

    public void setLeft_click(int left_click) {
        this.left_click = left_click;
    }

    public int getRight_click() {
        return right_click;
    }

    public void setRight_click(int right_click) {
        this.right_click = right_click;
    }

    public int getCtrl() {
        return ctrl;
    }

    public void setCtrl(int ctrl) {
        this.ctrl = ctrl;
    }
}
