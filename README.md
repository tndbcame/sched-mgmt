## ⚪︎ 概要

社員のスケジュール管理アプリです。社員情報を登録、編集ができ、それぞれのユーザーのスケジュールを見ることができます。

## ⚪︎ 機能一覧

| ログイン画面                                                                                                  | 管理画面                                                                                                 |
| ----------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| ![ログイン画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000000.JPG) | ![管理画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000001.JPG) |
| ログインする画面です。管理ユーザーなら管理画面、一般ユーザーならスケジュール画面に遷移します。                                    | 管理画面です。スケジュール画面、ユーザー登録画面に遷移できます。また、ユーザーの検索ができ、検索結果からユーザーを編集画面に遷移出来ます。ページングも実装しており、10人以降は次のページで確認できます。       |

| スケジュール画面                                                                                                  | ユーザー追新規登録画面                                                                                                 |
| ----------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| ![スケジュール画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000002.JPG) | ![ユーザー追新規登録画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000003.JPG) |
| スケジュール画面です。既に登録されているユーザーのスケジュールを確認、登録をすることができます。確認登録の通信は非同期通信(ajax)で実装しています。                                    | ユーザーの新規登録をすることができます。       |

| ユーザー編集画面                                                                                                  | パスワード編集画面                                                                                                 |
| ----------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| ![ユーザー編集画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000004.JPG) | ![パスワード編集画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000006.JPG) |
| ユーザー編集画面です。既に登録されているユーザー情報を編集することができます。この画面からパスワード編集画面に遷移することができます。                                    | パスワード編集画面です。ユーザーのパスワードを編集することができます。       |

| 編集完了画面                                                                                                  
| ----------------------------------------------------------------------------------------------------------- 
| ![編集完了画面](https://github.com/tndbcame/sched-mgmt/blob/main/sched_mgmt/assets/WS000007.JPG)   
| 編集完了画面です。ユーザー情報の編集や登録が終わった後に遷移する画面です。                                           

## ⚪︎ 使用技術

| Category       | Technology Stack                                                  |
| -------------- | ----------------------------------------------------------------- |
| Frontend       | html, css, javascrpt, jQuery(3.7.1)                               |
| Backend        | java(17.0.6), spring boot(3.2.5), spring security                 |
| Infrastructure | -                                                                 |
| Database       | postgresql(8.4)                                                   |
| Editor         | Visual Studio Code                                                |
| CI/CD          | -                                                                 |
| library        | thymeleaf, ajax, mybatis                                          |
| etc.           | Git, GitHub                                                       |


## ⚪︎ 今後の予定

GCP(Google Cloud Platform)を利用して仮想サーバーへデプロイする