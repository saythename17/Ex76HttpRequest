package com.icandothisallday2020.ex76httprequest;

public class BoardItem {
    String no,name,message,date;

    public BoardItem(String no, String name, String message, String date) {
        this.no = no;
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public BoardItem() {//기본으로 만드는 예비용 애개변수 없는 생성자
    }
}
