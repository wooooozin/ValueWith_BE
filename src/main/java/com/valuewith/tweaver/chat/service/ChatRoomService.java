package com.valuewith.tweaver.chat.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.chat.repository.ChatRoomRepository;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.groupMember.entity.GroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
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
    chatRoomRepository.deleteByTripGroupTripGroupId(tripGroupId);
  }

  public ChatRoom findByChatRoomId(Long chatRoomId) {
    // TODO: 커스텀 익셉션 해야함
    return chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
  }
}
