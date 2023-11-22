package com.valuewith.tweaver.chat.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.group.entity.TripGroup;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;

  public ChatRoom createChatRoom(TripGroup tripGroup) {
    ChatRoom chatRoom = ChatRoom.builder()
        .title(tripGroup.getName())
        .tripGroup(tripGroup)
        .build();
    return chatRoomRepository.save(chatRoom);
  }

  public void modifiedChatRoom(TripGroup tripGroup) {
    Optional<ChatRoom> foundChatRoom = chatRoomRepository.findByTripGroup(tripGroup);

    ChatRoom chatRoom = foundChatRoom.orElseThrow(() -> {
      throw new RuntimeException("수정할 채팅방 데이터가 존재하지 않습니다.");
    });

    chatRoom.updateChatRoom(tripGroup);

  }

  public void deleteChatRoom(Long tripGroupId) {
    ChatRoom chatRoom = chatRoomRepository.findByTripGroupTripGroupId(tripGroupId)
        .orElseThrow(() -> new RuntimeException("삭제 하려는 메세지의 채팅방이 존재하지 않습니다."));
    chatRoomRepository.deleteById(chatRoom.getChatRoomId());
  }

  public ChatRoom findByChatRoomId(Long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
  }

  public List<ChatRoom> findChatRoomListByTripGroupId(Long tripGroupId) {
    return chatRoomRepository.findChatRoomsByTripGroup_TripGroupId(tripGroupId);
  }
}
