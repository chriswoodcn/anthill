-- ############一张表只管一个sequence 借助auto_increment主键
-- 创建保存sequence的表
create table `t_user_id_sequence`
(
    `id`     bigint     not null auto_increment primary key,
    `t_text` varchar(5) not null default '' comment 'insert value'
);
-- 确保函数user_id_seq_func能够成功创建
-- 或者在配置文件/etc/my.cnf的[mysqld]或者my-default.ini文件中配置log_bin_trust_function_creators=1
set global log_bin_trust_function_creators=1;

-- 创建函数
delimiter &&
create function user_id_seq_func() returns bigint
begin
    declare sequence bigint;
    insert into t_user_id_sequence (t_text) values ('a');
    select last_insert_id() into sequence from dual;
    delete from t_user_id_sequence;
    return sequence;
end &&
delimiter ;

select user_id_seq_func() from dual;


-- ############一张表管理多个sequence 借助name主键区分 start_value记录当前值 increment_value记录步长
create table anthill_sequence
(
    name varchar(50) not null primary key,
    start_value bigint not null,
    increment_value bigint not null default 1
);

set global log_bin_trust_function_creators=1;

delimiter &&
create function nextVal(str varchar(50)) returns bigint
begin
    declare i bigint;
    set i=(select start_value from anthill_sequence where name=str);
    update anthill_sequence set start_value=i+increment_value where name=str;
    return i;
end &&

delimiter ;

DROP FUNCTION IF EXISTS nextVal;

insert into anthill_sequence values('test',1,1);
select nextVal('test') from dual;
