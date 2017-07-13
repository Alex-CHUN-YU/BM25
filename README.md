# BM25
![demo](https://github.com/Alex-CHUN-YU/BM25/blob/master/image/demo.png)</br></br></br>
資訊檢索(Information Retrieval)中Okapi BM25（BM代表最佳匹配）扮演著一種排名的方法，通常使用於搜索引擎，根據給定的搜索句子再找出排名匹配文檔最有關係之文檔。</br>
註:由於此為demo project故沒有，有效利用BM25該有之特性，它應適用於較長文件進行比對較為適合。</br>
* 計算文件與文件之間的相關性</br>


## 使用方式
Input:</br>
```
1.執行Main.java並輸入以下類似之句子(EX:如何設定手機上網?)
2.可到/src/main/resources/TestFile.txt中的資料集尋找句子修改做測試。
```
Output:</br>
```
Data中最接近的句子:﻿手機上網如何設定?
```

## 開發環境
Maven Project</br>
IDEA Version:IntelliJ IDEA 2017.1.2 x64</br>
JDK: java version "1.8.0_121"</br>
