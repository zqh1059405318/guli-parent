package com.atguigu.demo.excelTest;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        // 设置写入文件夹的地址和excel的文件名称
        String filename = "F:\\write.xlsx";
//        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());`
        //读操作
        EasyExcel.read(filename, DemoData.class, new listener()).sheet().doRead();
    }

    private static List<DemoData> getData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(Integer.toString(i));
            data.setSname("lucy" + i);
            list.add(data);
        }
        return list;
    }
}
