/*
 * The MIT License
 *
 * Copyright 2018 Saber.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.inapp123;

/**
 * Transfer插件 Server类 <br>
 * 用来表示一个服务器用的对象的类. <br>
 * 构造函数: <br>
 * idnum ID号，随意分配 之后可用getid方法获取 <br>
 * bcname 服务器在bc配置中的名字 之后可用getname方法获取 <br>
 * p 端口 之后可用getport方法获取 <br>
 * maxplayers 最大人数之后 可用getmaxplayer方法获取
 * @author Saber
 */
public class Transferserver{
    private final int id;
    private final int port;
    private final int maxplayer;
    public int onlineplayer;
    private final String name;
    Transferserver(int idnum,String bcname,int p,int maxplayers){
        this.id = idnum;
        this.port = p;
        this.name = bcname;
        this.maxplayer = maxplayers;
    }
    int getid(){
        return this.id;
    }
    int getport(){
        return this.port;
    }
    int getmaxplayer(){
        return this.maxplayer;
    }
    String getname(){
        return this.name;
    }
    int getonlineplayer(){
        return this.onlineplayer;
    }
}
