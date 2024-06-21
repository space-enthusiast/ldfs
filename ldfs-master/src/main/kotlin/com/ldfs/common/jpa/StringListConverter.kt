package com.ldfs.common.jpa

import jakarta.persistence.AttributeConverter

class StringListConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(p0: List<String>?): String {
        return p0?.joinToString(",") ?: ""
    }

    override fun convertToEntityAttribute(p0: String?): List<String> {
        return p0?.split(",") ?: emptyList()
    }
}
