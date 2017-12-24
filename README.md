# elasticsearch-analysis-openkoreantext

[![Build Status](https://travis-ci.org/open-korean-text/elasticsearch-analysis-openkoreantext.svg?branch=master)](https://travis-ci.org/open-korean-text/elasticsearch-analysis-openkoreantext)

한국어(한글)를 처리하는 Elasticsearch analyzer입니다. [open-korean-text](https://github.com/open-korean-text/open-korean-text) 한국어 처리엔진으로 작성되었습니다.

Korean analysis plugin that integrates [open-korean-text](https://github.com/open-korean-text/open-korean-text) module into Elasticsearch.

Elasticsearch 4.x 이하의 버전은 지원하지 않습니다.

## Install

```shell
$ cd ${ES_HOME}
$ bin/elasticsearch-plugin install {download URL}
```

설치 후 `bin/elasticsearch` 실행 시, `loaded plugin [elasticsearch-analysis-openkoreantext]` 라는 로그가 출력되는지 확인합니다.

**download URL 은 아래 [Compatible Versions](#compatible-versions)를 참고하여 Elasticsearch 버젼에 맞는 Plugin 버젼을 다운로드 받아야합니다.**

## Example
#### Input
```shell
curl -X POST 'http://localhost:9200/_analyze' -d '{
  "analyzer": "openkoreantext-analyzer",
  "text": "한국어를 처리하는 예시입니닼ㅋㅋ"
}'
```

#### Output
```json
{
  "tokens": [
    {
      "token": "한국어",
      "start_offset": 0,
      "end_offset": 3,
      "type": "Noun",
      "position": 0
    },
    {
      "token": "처리",
      "start_offset": 5,
      "end_offset": 7,
      "type": "Noun",
      "position": 1
    },
    {
      "token": "하다",
      "start_offset": 7,
      "end_offset": 9,
      "type": "Verb",
      "position": 2
    },
    {
      "token": "예시",
      "start_offset": 10,
      "end_offset": 12,
      "type": "Noun",
      "position": 3
    },
    {
      "token": "이다",
      "start_offset": 12,
      "end_offset": 15,
      "type": "Adjective",
      "position": 4
    },
    {
      "token": "ㅋㅋ",
      "start_offset": 15,
      "end_offset": 17,
      "type": "KoreanParticle",
      "position": 5
    }
  ]
}
```

Elasticsearch의 default analyzer를 사용했을 경우
```json
{
  "tokens": [
    {
      "token": "한국어를",
      "start_offset": 0,
      "end_offset": 4,
      "type": "<HANGUL>",
      "position": 0
    },
    {
      "token": "처리하는",
      "start_offset": 5,
      "end_offset": 9,
      "type": "<HANGUL>",
      "position": 1
    },
    {
      "token": "예시입니닼ㅋㅋ",
      "start_offset": 10,
      "end_offset": 17,
      "type": "<HANGUL>",
      "position": 2
    }
  ]
}
```

**실제 사용 방법은 [Elasicsearch analysis](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html)를 참고하세요.**

## User Dictionary
[기본사전](https://github.com/open-korean-text/open-korean-text/tree/master/src/main/resources/org/openkoreantext/processor/util) 이외에 사용자가 원하는 단어를 추가하여 사용할 수 있습니다. 예를들어 `말썽쟁이`를 분석하면 `말썽(Noun)`과 `쟁이(suffix)`로 추출되지만, 사전에 `말썽쟁이`를 추가하면 `말썽쟁이(Noun)`로 추출할 수 있습니다.

Analyzer Plugin을 설치하면 `{ES_HOME}/plugins/elasticserach-analysis-openkoreantext` 위치에 `dic/` 디렉토리를 찾을 수 있습니다. 해당 디렉토리 안에 사전 텍스트 파일을 추가하면 됩니다.

사전 텍스트 파일은 각 단어들을 줄바꿈하여 넣으면 됩니다. (단, 띄워쓰기는 단어로 인식하지 않습니다.)

```plain
# {ES_HOME}/plugins/elasticserach-analysis-openkoreantext/dic/sampledictionary
말썽쟁이
뚜쟁이
욕쟁이할머니
...
```


## Components
이 Analyzer는 몇 가지 [components](https://www.elastic.co/guide/en/elasticsearch/reference/current/analyzer-anatomy.html)로 구성되어 있습니다.

**Charater Filter**
* openkoreantext-normalizer
  * 구어체를 표준화 합니다.
  > 훌쩍훌쩍훌쩍훌쩍 -> 훌쩍훌쩍, 하겟다 -> 하겠다, 안됔ㅋㅋㅋ -> 안돼ㅋㅋ

**Tokenizer**
* openkoreantext-tokenizer
  * 문장을 토큰화 합니다.
  > 한국어를 처리하는 예시입니다 ㅋㅋ -> [한국어, 를, 처리, 하는, 예시, 입니다, ㅋㅋ]

**Token Filter**
* openkoreantext-stemmer
  * 형용사와 동사를 스테밍합니다.
  > 새로운 스테밍을 추가했었다. -> [새롭다, 스테밍, 을, 추가하다, .]

* openkoreantext-redundant-filter
  * 접속사, 공백(띄워쓰기), 조사, 마침표 등을 제거합니다.
  > 그리고 이것은 예시, 또는 예로써, 한국어를 처리하기 -> [예시, 예, 한국어, 처리, 하다]

* openkoreantext-phrase-extractor
  * 어구를 추출합니다.
  > 한국어를 처리하는 예시입니다 ㅋㅋ -> [한국어, 처리, 예시, 처리하는 예시]

**Analyzer**

[`openkoreantext-normalizer`] -> [`openkoreantext-tokenizer`] -> [`openkoreantext-stemmer`, `openkoreantext-redundant-filter`,  `classic`, `length`, `lowercase`]

* 이 analyzer에는 `openkoreantext-phrase-extractor`가 기본 token filter로 적용되어있지 않습니다.
* custom analyzer 구성을 원하시면 [custom analyzer](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-custom-analyzer.html)를 참고하세요.

## Compatible Versions

| Elasticsearch | open-korean-text | Download URL |
|:----:|:----:|:----|
| 6.1.1 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/6.1.1/elasticsearch-analysis-openkoreantext-6.1.1.2-plugin.zip |
| 6.1.0 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/6.1.1/elasticsearch-analysis-openkoreantext-6.1.0.2-plugin.zip |
| 6.0.0 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/6.0.0.2/elasticsearch-analysis-openkoreantext-6.0.0.2-plugin.zip |
| 5.6.5 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/6.1.1/elasticsearch-analysis-openkoreantext-5.6.5.2-plugin.zip |
| 5.6.4 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.6.4.2/elasticsearch-analysis-openkoreantext-5.6.4.2-plugin.zip |
| 5.6.3 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.6.4.2/elasticsearch-analysis-openkoreantext-5.6.3.2-plugin.zip |
| 5.6.2 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/v5.6.x/elasticsearch-analysis-openkoreantext-5.6.2.2-plugin.zip |
| 5.6.1 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/v5.6.x/elasticsearch-analysis-openkoreantext-5.6.1.2-plugin.zip |
| 5.6.0 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/v5.6.x/elasticsearch-analysis-openkoreantext-5.6.0.2-plugin.zip |
| 5.5.2 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.5.2.2/elasticsearch-analysis-openkoreantext-5.5.2.2-plugin.zip |
| 5.5.1 | 2.1.0 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.5.1.2.1/elasticsearch-analysis-openkoreantext-5.5.1.2-plugin.zip |
| 5.5.0 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.5.0.2/elasticsearch-analysis-openkoreantext-5.5.0.2-plugin.zip |
| 5.4.3 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.2.2/elasticsearch-analysis-openkoreantext-5.4.3.2-plugin.zip |
| 5.4.2 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.2.2/elasticsearch-analysis-openkoreantext-5.4.2.2-plugin.zip |
| 5.4.1 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.1.2/elasticsearch-analysis-openkoreantext-5.4.1.2-plugin.zip |
| 5.4.0 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.4.0.2-plugin.zip |
| 5.3.2 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.3.2.2-plugin.zip |
| 5.3.1 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.3.1.2-plugin.zip |
| 5.3.0 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.3.0.2-plugin.zip |
| 5.2.2 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.2.2.2-plugin.zip |
| 5.2.1 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.2.1.2-plugin.zip |
| 5.1.2 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.1.2.2-plugin.zip |
| 5.1.1 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.1.1.2-plugin.zip |
| 5.1.0 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.1.0.2-plugin.zip |
| 5.0.2 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.0.2.2-plugin.zip |
| 5.0.1 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.0.1.2-plugin.zip |
| 5.0.0 | 2.0.1 | https://github.com/open-korean-text/elasticsearch-analysis-openkoreantext/releases/download/5.4.0.2/elasticsearch-analysis-openkoreantext-5.0.0.2-plugin.zip |


* 5.0.0 미만의 버젼은 지원하지 않습니다. open-korean-text로 작성된 다른 플러그인은 참조하시기 바랍니다.  
  * [tkt-elasticsearch](https://github.com/open-korean-text/open-korean-text-elastic-search)
  * [elasticsearch-twitter-korean](https://github.com/jobplanet/elasticsearch-twitter-korean)

## License
Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
