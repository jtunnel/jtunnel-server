package cn.forwode.tunnel;

import java.util.HashMap;
import java.util.Map;

public class ControlThreadManager{

    private static Map<String, Thread> threadsMap = new HashMap<String, Thread>();

    public static Thread getControlThread(String port){
        return threadsMap.get(port);
    }

    public static void addControlThread(String port, Thread controlThread){
        threadsMap.put(port, controlThread);
    }
}