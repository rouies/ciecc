package com.ciecc.common.converter;

/*
 * Name      : Direction
 * Creator   : louis zhang
 * Function  : 表示类型转换方向的枚举
 * Date      : 2016-1-18
 */
enum Direction {
    FROM(0),TO(1);

    private int direction;

    Direction(int direction){
        this.direction = direction;
    }

    /**
     * 根据转换方向执行转换逻辑
     * @param obj 要转换的对象
     * @param converter 转换器
     * @return 转换后的结果
     */
    public Object execute(Object obj,IConverter converter){
        if(this.direction == 0){
            return converter.from(obj);
        } else{
            return converter.to(obj);
        }
    }

}
