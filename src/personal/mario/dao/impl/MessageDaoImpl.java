package personal.mario.dao.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;
import personal.mario.bean.ChatMessage;
import personal.mario.dao.MessageDao;

@Repository("messageDao")
public class MessageDaoImpl implements MessageDao {

	@Autowired
	private RedisTemplate<String, ChatMessage> redisTemplate;
	
	@Override
	public void save(ChatMessage message) {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<ChatMessage>(ChatMessage.class));
		//初始化bean时运行 序列化类涉及泛型所以配置文件注入不适用
		redisTemplate.afterPropertiesSet();

		ListOperations<String, ChatMessage> ops = redisTemplate.opsForList();
		ops.leftPush("chatRecord", message);
	}

	@Override
	public List<ChatMessage> getList() {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<ChatMessage>(ChatMessage.class));
		redisTemplate.afterPropertiesSet();
		
		ListOperations<String, ChatMessage> ops = redisTemplate.opsForList();
		return ops.range("chatRecord", 0, ops.size("chatReocrd") - 1);
	}
}
