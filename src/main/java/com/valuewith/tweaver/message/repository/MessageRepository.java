package com.valuewith.tweaver.message.repository;

import com.valuewith.tweaver.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  void deleteByChatRoomChatRoomId(Long chatRoomId);
}
