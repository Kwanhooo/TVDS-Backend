# The TVDS Project

## —— API文档

1. ### /test`不会在生产环境中使用`

    1. ##### /uploadCarriage

        - POST

        - MultipartFile

        - 响应体

          ```json
          {
            "code": 0,
            "message": "SUCCESS",
            "timestamp": 1677912290600,
            "data": {
              "dbId": 1677912290527,
              "id": "3907_20220123001_1_1",
              "inspectionSeq": 3907,
              "cameraNumber": 1,
              "carriageId": null,
              "carriageNo": 1,
              "model": null,
              "status": 0,
              "compositeUrl": "composite/3907/20220123001_1_1.jpg",
              "compositeTime": "2022-01-23T00:00:00",
              "alignedUrl": null,
              "alignTime": null,
              "createTime": "2023-03-04T14:44:50.53",
              "updateTime": "2023-03-04T14:44:50.53",
              "isDeleted": null
            }
          }
          ```

2. ### /retrieve

    1. ##### /carriage/{currentPage}/{pageSize}

        1. POST

        2. JSON

            - 请求体

              ```json
              {
                "startDate": "2022-01-01",
                "endDate": "2022-01-01",
                "inspectionSeq": "",
                "carriageNo": "",
                "id": ""
              }
              ```

        3. 响应体

           ```json
           {
             "code": 0,
             "message": "SUCCESS",
             "timestamp": 1677912551899,
             "data": {
               "currentPage": 1,
               "totalPage": 2,
               "pageSize": 3,
               "content": [
                 {
                   "dbId": 1677908130570,
                   "id": "3907_20220123001_1_1",
                   "inspectionSeq": 3907,
                   "cameraNumber": 1,
                   "carriageId": 5327501,
                   "carriageNo": 1,
                   "model": "X70",
                   "status": 0,
                   "compositeUrl": null,
                   "compositeTime": "2022-01-23T00:00:00",
                   "alignedUrl": null,
                   "alignTime": null,
                   "createTime": "2023-03-04T13:35:31",
                   "updateTime": "2023-03-04T13:35:31",
                   "isDeleted": 0,
                   "url": "composite/3907/20220123001_1_1.jpg"
                 },
                 {
                   "dbId": 1677908133448,
                   "id": "3907_20220123001_1_1",
                   "inspectionSeq": 3907,
                   "cameraNumber": 1,
                   "carriageId": null,
                   "carriageNo": 1,
                   "model": "unknown",
                   "status": 0,
                   "compositeUrl": null,
                   "compositeTime": "2022-01-23T00:00:00",
                   "alignedUrl": null,
                   "alignTime": null,
                   "createTime": "2023-03-04T13:35:33",
                   "updateTime": "2023-03-04T13:35:33",
                   "isDeleted": 0,
                   "url": "composite/3907/20220123001_1_1.jpg"
                 },
                 {
                   "dbId": 1677908134188,
                   "id": "3907_20220123001_1_1",
                   "inspectionSeq": 3907,
                   "cameraNumber": 1,
                   "carriageId": null,
                   "carriageNo": 1,
                   "model": "unknown",
                   "status": 0,
                   "compositeUrl": null,
                   "compositeTime": "2022-01-23T00:00:00",
                   "alignedUrl": null,
                   "alignTime": null,
                   "createTime": "2023-03-04T13:35:34",
                   "updateTime": "2023-03-04T13:35:34",
                   "isDeleted": 0,
                   "url": "composite/3907/20220123001_1_1.jpg"
                 }
               ]
             }
           }
           ```

    2. ##### /carriage/byIds/{currentPage}/{pageSize}

        1. POST

        2. JSON

            - 请求体

              ```json
              [
                "3907_20220123001_1_1"
              ]
              ```

        3. 响应体

           ```
           {
             "code": 0,
             "message": "SUCCESS",
             "timestamp": 1677912627585,
             "data": {
               "currentPage": 1,
               "totalPage": 2,
               "pageSize": 2,
               "content": [
                 {
                   "dbId": 1677908130570,
                   "id": "3907_20220123001_1_1",
                   "inspectionSeq": 3907,
                   "cameraNumber": 1,
                   "carriageId": 5327501,
                   "carriageNo": 1,
                   "model": "X70",
                   "status": 0,
                   "compositeUrl": null,
                   "compositeTime": "2022-01-23T00:00:00",
                   "alignedUrl": null,
                   "alignTime": null,
                   "createTime": "2023-03-04T13:35:31",
                   "updateTime": "2023-03-04T13:35:31",
                   "isDeleted": 0,
                   "url": "composite/3907/20220123001_1_1.jpg"
                 },
                 {
                   "dbId": 1677908133448,
                   "id": "3907_20220123001_1_1",
                   "inspectionSeq": 3907,
                   "cameraNumber": 1,
                   "carriageId": null,
                   "carriageNo": 1,
                   "model": "unknown",
                   "status": 0,
                   "compositeUrl": null,
                   "compositeTime": "2022-01-23T00:00:00",
                   "alignedUrl": null,
                   "alignTime": null,
                   "createTime": "2023-03-04T13:35:33",
                   "updateTime": "2023-03-04T13:35:33",
                   "isDeleted": 0,
                   "url": "composite/3907/20220123001_1_1.jpg"
                 }
               ]
             }
           }
           ```

3. ### /tree

    1. ##### /carriage

        1. POST

        2. JSON

            - 请求体

              ```json
              {
                "startDate": "2022-01-01",
                "endDate": "2025-01-01",
                "inspectionSeq": "",
                "carriageNo": "",
                "id": ""
              }
              ```

        3. 响应体

           ```json
           {
             "code": 0,
             "message": "SUCCESS",
             "timestamp": 1677912773221,
             "data": {
               "label": "ROOT",
               "children": [
                 {
                   "label": "X70",
                   "children": [
                     {
                       "label": "2022-01-23T00:00",
                       "children": [
                         {
                           "label": "3907",
                           "children": [
                             {
                               "label": "1",
                               "children": [
                                 {
                                   "label": "3907_20220123001_1_1",
                                   "children": []
                                 }
                               ]
                             }
                           ]
                         }
                       ]
                     }
                   ]
                 },
                 {
                   "label": "unknown",
                   "children": [
                     {
                       "label": "2022-01-23T00:00",
                       "children": [
                         {
                           "label": "3907",
                           "children": [
                             {
                               "label": "1",
                               "children": [
                                 {
                                   "label": "3907_20220123001_1_1",
                                   "children": []
                                 }
                               ]
                             }
                           ]
                         }
                       ]
                     }
                   ]
                 }
               ]
             }
           }
           ```

4. ### vision

    1. ##### /ocr/{dbId}

        1. POST

        2. PathVariable (示例：/vision/ocr/1677908130570)

        3. 响应体

           ```json
           {
             "code": 0,
             "message": "SUCCESS",
             "timestamp": 1677912824405,
             "data": {
               "message": "OCR识别成功",
               "data": {
                 "dbId": 1677908130570,
                 "id": "3907_20220123001_1_1",
                 "inspectionSeq": 3907,
                 "cameraNumber": 1,
                 "carriageId": 5327501,
                 "carriageNo": 1,
                 "model": "X70",
                 "status": 0,
                 "compositeUrl": null,
                 "compositeTime": "2022-01-23T00:00:00",
                 "alignedUrl": null,
                 "alignTime": null,
                 "createTime": "2023-03-04T13:35:31",
                 "updateTime": "2023-03-04T13:35:31",
                 "isDeleted": 0,
                 "url": "composite/3907/20220123001_1_1.jpg"
               },
               "succeed": true
             }
           }
           ```

         

