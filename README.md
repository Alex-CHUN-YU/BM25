# BM25
![demo](https://github.com/Alex-CHUN-YU/BM25/blob/master/image/demo.png)</br></br>
資訊檢索(Information Retrieval)中 Okapi BM25 （BM代表最佳匹配）扮演著一種排名的方法，通常使用於搜索引擎，根據給定的搜索句子再找出排名匹配文檔最有關係之文檔。順帶一提 BM25 它所計算的方式，主要還是詞頻關係與分析並未考慮到句子架構語法故可以把它(文檔)想像成雜亂無序的 Bag 去進行排序。</br>

## 簡單摘要
* 資訊檢索(Information Retrieval)
* 詞頻關係與分析並未考慮到句子架構語法
* 計算文件與文件之間的相關性排名(Ranking Function)

## 使用方式
Input:</br>
```
1.執行 BM25Test.java 並輸入以下類似之句子(EX:如何設定手機上網?)
2.可到/src/main/resources/TestFile.txt中的資料集尋找句子修改做測試。
註: 可在 TestFile.txt 修改成自己的 database 每個 document(文檔) 請用跳行分開
```
Output:</br>
```
Data中最接近的句子:﻿手機上網如何設定?
分數為:7.600866591869731
```

## 開發環境
Maven Project</br>
IDEA Version:IntelliJ IDEA 2017.1.2 x64</br>
JDK: java version "1.8.0_121"</br>

## 補充
註:由於此為 demo project 故沒有，有效利用 BM25 該有之特性，它應適用於較長文件進行比對較為適合。</br>
