package com.valuewith.tweaver.message.repository;

import com.valuewith.tweaver.message.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  void deleteByChatRoomChatRoomId(Long chatRoomId);

  List<Message> findAllByChatRoom_ChatRoomId(Long chatRoomId);
}
