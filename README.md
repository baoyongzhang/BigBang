# BigBang

本 Demo 模仿了 Smartisan OS 的 BigBang 功能。App 打开会从剪切板读取文字并显示，长按会弹出 BigBang 页面进行分词显示。如果剪切板没有内容会显示一段默认的文字。
后台接口基于 [node-segment](https://github.com/leizongmin/node-segment) 实现分词。

__由于服务是我自己搭建的，服务器性能有限，如果长时间没有显示出来可能是服务挂了，接口仅用于演示，请大家不要用在其他地方，不要高频的请求，谢谢。__

<img src="https://github.com/baoyongzhang/BigBang/blob/master/design/bigbang_demo.gif" width=320/>
<img src="https://github.com/baoyongzhang/BigBang/blob/master/design/gitbang_demo_wechat.gif" width=320/>

### 微信支持

现在支持在微信中使用 BigBang，提供两种方案。
* 免 root 方案：需要在系统设置中开启权限，目前仅支持聊天气泡文字，点击文字就可以打开 BigBang 页面。
* Xposed 方案：需要安装 Xposed 框架，在 Xposed 中可以找到 BigBang 模块，目前支持聊天气泡文字和朋友全文字，点击两下触发。

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
