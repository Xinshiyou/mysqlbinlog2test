# 解析MySQL BinLog实体

# 主要功能
1. 连接MySQL数据库
2. 通过引用Jar解析Binlog
3. 封装Binlog为JSON格式
4. JSON数据发送到Kafka中

# 目前版本存在问题
1. 每次都需要更新配置文件，性能较差
2. 封装存在问题， 目前使用GenericMessage作为消息传递，后期版本使用String作为封装
3. 配置性方面，需要更好的通用化配置


