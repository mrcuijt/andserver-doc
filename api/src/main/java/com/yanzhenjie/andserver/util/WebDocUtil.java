package com.yanzhenjie.andserver.util;

import com.yanzhenjie.andserver.entity.FileModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WebDocUtil {
    public static List<List<FileModel>> generateGroupListByFileType(List<FileModel> paramList) {
        return GroupByExtensionUtil.divider(paramList, new Comparator<FileModel>() {
            public int compare(FileModel param1FileModel1, FileModel param1FileModel2) {
                return param1FileModel1.getFileType().compareTo(param1FileModel2.getFileType());
            }
        });
    }

    public static List<FileModel> mergeAndSortFileModel(List<List<FileModel>> paramList) {
        ArrayList<FileModel> arrayList = new ArrayList();
        for (List<FileModel> list : paramList) {
            Collections.sort(list, new Comparator<FileModel>() {
                public int compare(FileModel param1FileModel1, FileModel param1FileModel2) {
                    if (param1FileModel1.getFileName().length() == param1FileModel2.getFileName().length())
                        return param1FileModel1.getFileName().compareTo(param1FileModel2.getFileName());
                    return param1FileModel1.getFileName().length() - param1FileModel2.getFileName().length();
                }
            });
            arrayList.addAll(list);
        }
        return (List)arrayList;
    }
}
