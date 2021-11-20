package api.art.deviant.model.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object NotBlankStringSerializer : KSerializer<String?> {
    override fun deserialize(decoder: Decoder): String? {
        return decoder.decodeString().takeUnless { it.isBlank() }
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "kotlin.String?",
        PrimitiveKind.STRING,
    )

    override fun serialize(encoder: Encoder, value: String?) {
    }
}
