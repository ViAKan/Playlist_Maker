package com.example.playlistmaker.sharing

import com.example.playlistmaker.sharing.data.model.EmailDataDto
import com.example.playlistmaker.sharing.domain.model.EmailData

object EmailDataMapper {

    fun EmailDataDto.toEmailData() = EmailData(
        email = email,
        subject = subject,
        body = body
    )

    fun EmailData.toDto() = EmailDataDto(
    email = email,
    subject = subject,
    body = body
    )

}