/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitepowered.granite.utils.json;

import com.google.gson.*;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.text.action.GraniteClickAction;
import org.granitepowered.granite.impl.text.action.GraniteHoverAction;
import org.granitepowered.granite.impl.text.action.GraniteShiftClickAction;
import org.granitepowered.granite.impl.text.format.GraniteTextColor;
import org.granitepowered.granite.impl.text.format.GraniteTextStyle;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.granitepowered.granite.impl.text.message.GraniteMessageBuilder;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.MessageBuilder;

import java.lang.reflect.Type;

public class MessageJson implements JsonSerializer<GraniteMessage<?>>, JsonDeserializer<GraniteMessage<?>> {
    @Override
    public GraniteMessage<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            return (GraniteMessage<?>) new GraniteMessageBuilder.GraniteTextMessageBuilder().content(json.getAsString()).build();
        }

        JsonObject src = json.getAsJsonObject();

        MessageBuilder<?> builder;
        if (src.has("text")) {
            builder = new GraniteMessageBuilder.GraniteTextMessageBuilder().content(src.get("text").getAsString());
        } else {
            throw new NotImplementedException("");
        }

        // TODO: add the other 3 types

        builder = builder.color(src.has("color") ? TextColors.RESET : GraniteTextColor.valueOf(src.get("color").getAsString().toUpperCase()));

        boolean bold, italic, underlined, strikethrough, obfuscated;
        bold = src.has("bold") && src.get("bold").getAsBoolean();
        italic = src.has("italic") && src.get("italic").getAsBoolean();
        underlined = src.has("underlined") && src.get("underlined").getAsBoolean();
        strikethrough = src.has("strikethrough") && src.get("strikethrough").getAsBoolean();
        obfuscated = src.has("obfuscated") && src.get("obfuscated").getAsBoolean();

        builder = builder.style(new GraniteTextStyle(
                bold ? GraniteTextStyle.TextStyleMode.APPLIED : GraniteTextStyle.TextStyleMode.NEUTRAL,
                italic ? GraniteTextStyle.TextStyleMode.APPLIED : GraniteTextStyle.TextStyleMode.NEUTRAL,
                underlined ? GraniteTextStyle.TextStyleMode.APPLIED : GraniteTextStyle.TextStyleMode.NEUTRAL,
                strikethrough ? GraniteTextStyle.TextStyleMode.APPLIED : GraniteTextStyle.TextStyleMode.NEUTRAL,
                obfuscated ? GraniteTextStyle.TextStyleMode.APPLIED : GraniteTextStyle.TextStyleMode.NEUTRAL
        ));

        if (src.has("hoverEvent")) {
            builder = builder.onHover((HoverAction<?>) context.deserialize(src.get("hoverEvent"), GraniteHoverAction.class));
        }

        if (src.has("clickEvent")) {
            builder = builder.onClick((ClickAction<?>) context.deserialize(src.get("clickEvent"), GraniteClickAction.class));
        }

        if (src.has("insertion")) {
            builder = builder.onShiftClick(new GraniteShiftClickAction.GraniteInsertText(src.get("insertion").getAsString()));
        }

        if (src.has("extra")) {
            for (JsonElement elem : src.getAsJsonArray("extra")) {
                builder = builder.append((Message[]) context.deserialize(elem, GraniteMessage.class));
            }
        }

        return (GraniteMessage<?>) builder.build();
    }

    @Override
    public JsonElement serialize(GraniteMessage<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject out = new JsonObject();

        if (src instanceof GraniteMessage.GraniteText) {
            out.add("text", context.serialize(src.getContent()));
        }
        // TODO: add the other 3 types

        if (src.getHoverAction().isPresent()) {
            out.add("hoverEvent", context.serialize(src.getHoverAction().get()));
        }

        if (src.getClickAction().isPresent()) {
            out.add("clickEvent", context.serialize(src.getClickAction().get()));
        }

        if (src.getShiftClickAction().isPresent()) {
            out.add("insertion", context.serialize(src.getShiftClickAction().get().getResult()));
        }

        TextStyle style = src.getStyle();
        if (style.is(TextStyles.BOLD)) out.add("bold", context.serialize(true));
        if (style.is(TextStyles.ITALIC)) out.add("italic", context.serialize(true));
        if (style.is(TextStyles.UNDERLINE)) out.add("underlined", context.serialize(true));
        if (style.is(TextStyles.STRIKETHROUGH)) out.add("strikethrough", context.serialize(true));
        if (style.is(TextStyles.OBFUSCATED)) out.add("obfuscated", context.serialize(true));

        out.add("color", context.serialize(((TextColor.Base) src.getColor()).getName().toLowerCase()));

        if (!src.getChildren().isEmpty()) {
            JsonArray extra = new JsonArray();

            for (Message child : src.getChildren()) {
                extra.add(context.serialize(child, GraniteMessage.class));
            }

            out.add("extra", extra);
        }

        return out;
    }
}
