package com.gsl.tech.mockorder;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description: 事件实体类
 * @date 2021/5/23 11:44
 */
@Data
@ToString
@NoArgsConstructor
public class OrderEvent {

    private String value;
}
