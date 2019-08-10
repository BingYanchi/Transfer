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
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
/**
 * Transfer 配置文件加载器
 * @author Saber
 */
public class Transfercfgloader {
    private final FileConfiguration config;
    private final HashMap<String,Transfercollection> map;
    public Transfercollection transcolle[];
    public String please_wait;
    public String must_player;
    public String in_queue;
    public String not_in_queue;
    public String leave_queue;
    public String unknow_queue;
    Transfercfgloader(Plugin thisplugin){
        //初始化配置.
        map = new HashMap<>();
        if(!thisplugin.getDataFolder().exists())
        {
            thisplugin.saveDefaultConfig();
        }
        //语言文本 加载.
        config = thisplugin.getConfig();
        please_wait = config.getString("lang.please_wait");
        must_player = config.getString("lang.must_player");
        in_queue = config.getString("lang.in_queue");
        not_in_queue = config.getString("lang.not_in_queue");
        leave_queue = config.getString("lang.leave_queue");
        unknow_queue = config.getString("lang.unknow_queue");
        //集合 加载.
        transcolle = new Transfercollection[config.getStringList("collection").size()];
        for(int i=0;i<transcolle.length;i++){
            transcolle[i] = new Transfercollection(config,config.getStringList("collection").get(i),thisplugin);
            map.put(config.getStringList("collection").get(i), transcolle[i]);
        }
    }

    /**
     * 根据名字 获取集合对象
     * @param name collection名字
     * @return Transfercollection 集合对象
     */
    public Transfercollection getcollectionbyname(String name){
        return map.get(name);
    }

    /**
     * 名字所对应的集合是否存在
     * @param name 名字
     * @return true/false
     */
    public boolean iscollectionexist(String name){
        return map.containsKey(name);
    }
}
