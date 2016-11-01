# BigBang

本 Demo 模仿了 Smartisan OS 的 BigBang 功能。App 打开会从剪切板读取文字并显示，长按会弹出 BigBang 页面进行分词显示。如果剪切板没有内容会显示一段默认的文字。
后台接口基于 [node-segment](https://github.com/leizongmin/node-segment) 实现分词。

<img src="https://github.com/baoyongzhang/BigBang/blob/master/design/bigbang_demo.gif" width=320/>
<img src="https://github.com/baoyongzhang/BigBang/blob/master/design/gitbang_demo_wechat.gif" width=320/>

## 特性
* 会监听剪切板复制内容，弹出按钮点击打开 BigBang 页面分词显示。
* 支持本地分词，内置本地分词引擎，离线也可以分词。
* 支持微信内使用。

### 微信支持

现在支持在微信中使用 BigBang，提供两种方案。
* 免 root 方案：需要在系统设置中开启权限，目前仅支持聊天气泡文字，点击文字就可以打开 BigBang 页面。
* Xposed 方案：需要安装 Xposed 框架，在 Xposed 中可以找到 BigBang 模块，目前支持聊天气泡文字和朋友圈文字，点击两下触发。

## Download
<a href='https://play.google.com/store/apps/details?id=com.baoyz.bigbang&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' width=160 src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

# Contributors

### [zzz40500](https://github.com/zzz40500/)
```
增加了 Xposed 的支持。
```

# License

``` 
MIT License

Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
