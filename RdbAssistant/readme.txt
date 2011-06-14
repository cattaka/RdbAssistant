Copyright 2011 Takao Sumitomo. All rights reserved.

    RdbAssistant : Relational DataBase Assistant
    URL http://www.cattaka.net/

◇概要
    OracleとMySQLとSQLiteに対応したJavaで動作するSQLエディタです。

◇動作環境
    ・Java(TM) 2 SDK, Standard Edition Version 1.5.0以上

◇開発環境
    ・Debian Linux(Squeeze)
    ・Linux version 2.6.x
    ・Java(TM) 2 SDK, Standard Edition Version 1.5.x
    ・Eclipse 3.3.x
    ・Oracle Database 10g Express Universal Edition(10.2)
    ・MySql 5.0.x

◇初期設定手順
    1.設定
        メニューバーの編集から設定を選び、
        使用したいRDBMSのJDBCのJarファイルを指定してください。
    2.接続先の設定
        メインウインドウの新規接続タブの新規ボタンを押し、
        使用したいRDBMSを選択した後、それぞれ必要な情報を入力してください。
        最後にOKボタンを押すと接続先リストに入力した内容が追加されます。
    3.接続
        接続先リストから対象を選択して接続ボタンを押すと
       RDBMSへの接続が開始されます。

◇操作方法
    ◆SQLエディタ
       SQLタブを選択するとSQLの入力と実行が行えます。
    ◆SQLエディタ
        スクリプトタブを選択するとスクリプトの作成と実行が行えます。
        スクリプト機能を使用するためには設定ダイアログにて
       tools.jarとスクリプト作業用ディレクトリが正しく設定されている必要があります。
        スクリプトで使用できる関数等は付属のドキュメントをご覧下さい。
