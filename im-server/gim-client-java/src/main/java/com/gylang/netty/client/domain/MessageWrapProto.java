// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MessageWarp.proto

package com.gylang.netty.client.domain;

public final class MessageWrapProto {
  private MessageWrapProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ModelOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Model)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string key = 1;</code>
     */
    String getKey();
    /**
     * <code>optional string key = 1;</code>
     */
    com.google.protobuf.ByteString
        getKeyBytes();

    /**
     * <code>optional string content = 2;</code>
     */
    String getContent();
    /**
     * <code>optional string content = 2;</code>
     */
    com.google.protobuf.ByteString
        getContentBytes();

    /**
     * <code>optional string receiver = 3;</code>
     */
    String getReceiver();
    /**
     * <code>optional string receiver = 3;</code>
     */
    com.google.protobuf.ByteString
        getReceiverBytes();

    /**
     * <code>optional string type = 4;</code>
     */
    String getType();
    /**
     * <code>optional string type = 4;</code>
     */
    com.google.protobuf.ByteString
        getTypeBytes();

    /**
     * <code>optional string receiverType = 5;</code>
     */
    String getReceiverType();
    /**
     * <code>optional string receiverType = 5;</code>
     */
    com.google.protobuf.ByteString
        getReceiverTypeBytes();

    /**
     * <code>optional bytes bytes = 6;</code>
     */
    com.google.protobuf.ByteString getBytes();
  }
  /**
   * Protobuf type {@code Model}
   */
  public  static final class Model extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:Model)
      ModelOrBuilder {
    // Use Model.newBuilder() to construct.
    private Model(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Model() {
      key_ = "";
      content_ = "";
      receiver_ = "";
      type_ = "";
      receiverType_ = "";
      bytes_ = com.google.protobuf.ByteString.EMPTY;
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private Model(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              String s = input.readStringRequireUtf8();

              key_ = s;
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();

              content_ = s;
              break;
            }
            case 26: {
              String s = input.readStringRequireUtf8();

              receiver_ = s;
              break;
            }
            case 34: {
              String s = input.readStringRequireUtf8();

              type_ = s;
              break;
            }
            case 42: {
              String s = input.readStringRequireUtf8();

              receiverType_ = s;
              break;
            }
            case 50: {

              bytes_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MessageWrapProto.internal_static_com_gylang_sdk_proto_Model_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MessageWrapProto.internal_static_com_gylang_sdk_proto_Model_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Model.class, Builder.class);
    }

    public static final int KEY_FIELD_NUMBER = 1;
    private volatile Object key_;
    /**
     * <code>optional string key = 1;</code>
     */
    public String getKey() {
      Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        key_ = s;
        return s;
      }
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public com.google.protobuf.ByteString
        getKeyBytes() {
      Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int CONTENT_FIELD_NUMBER = 2;
    private volatile Object content_;
    /**
     * <code>optional string content = 2;</code>
     */
    public String getContent() {
      Object ref = content_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        content_ = s;
        return s;
      }
    }
    /**
     * <code>optional string content = 2;</code>
     */
    public com.google.protobuf.ByteString
        getContentBytes() {
      Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int RECEIVER_FIELD_NUMBER = 3;
    private volatile Object receiver_;
    /**
     * <code>optional string receiver = 3;</code>
     */
    public String getReceiver() {
      Object ref = receiver_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        receiver_ = s;
        return s;
      }
    }
    /**
     * <code>optional string receiver = 3;</code>
     */
    public com.google.protobuf.ByteString
        getReceiverBytes() {
      Object ref = receiver_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        receiver_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TYPE_FIELD_NUMBER = 4;
    private volatile Object type_;
    /**
     * <code>optional string type = 4;</code>
     */
    public String getType() {
      Object ref = type_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        type_ = s;
        return s;
      }
    }
    /**
     * <code>optional string type = 4;</code>
     */
    public com.google.protobuf.ByteString
        getTypeBytes() {
      Object ref = type_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        type_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int RECEIVERTYPE_FIELD_NUMBER = 5;
    private volatile Object receiverType_;
    /**
     * <code>optional string receiverType = 5;</code>
     */
    public String getReceiverType() {
      Object ref = receiverType_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        receiverType_ = s;
        return s;
      }
    }
    /**
     * <code>optional string receiverType = 5;</code>
     */
    public com.google.protobuf.ByteString
        getReceiverTypeBytes() {
      Object ref = receiverType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        receiverType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int BYTES_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString bytes_;
    /**
     * <code>optional bytes bytes = 6;</code>
     */
    public com.google.protobuf.ByteString getBytes() {
      return bytes_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getKeyBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, key_);
      }
      if (!getContentBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, content_);
      }
      if (!getReceiverBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, receiver_);
      }
      if (!getTypeBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, type_);
      }
      if (!getReceiverTypeBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, receiverType_);
      }
      if (!bytes_.isEmpty()) {
        output.writeBytes(6, bytes_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getKeyBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, key_);
      }
      if (!getContentBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, content_);
      }
      if (!getReceiverBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, receiver_);
      }
      if (!getTypeBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, type_);
      }
      if (!getReceiverTypeBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, receiverType_);
      }
      if (!bytes_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, bytes_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof Model)) {
        return super.equals(obj);
      }
      Model other = (Model) obj;

      boolean result = true;
      result = result && getKey()
          .equals(other.getKey());
      result = result && getContent()
          .equals(other.getContent());
      result = result && getReceiver()
          .equals(other.getReceiver());
      result = result && getType()
          .equals(other.getType());
      result = result && getReceiverType()
          .equals(other.getReceiverType());
      result = result && getBytes()
          .equals(other.getBytes());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + KEY_FIELD_NUMBER;
      hash = (53 * hash) + getKey().hashCode();
      hash = (37 * hash) + CONTENT_FIELD_NUMBER;
      hash = (53 * hash) + getContent().hashCode();
      hash = (37 * hash) + RECEIVER_FIELD_NUMBER;
      hash = (53 * hash) + getReceiver().hashCode();
      hash = (37 * hash) + TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getType().hashCode();
      hash = (37 * hash) + RECEIVERTYPE_FIELD_NUMBER;
      hash = (53 * hash) + getReceiverType().hashCode();
      hash = (37 * hash) + BYTES_FIELD_NUMBER;
      hash = (53 * hash) + getBytes().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static Model parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Model parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Model parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Model parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Model parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Model parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static Model parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static Model parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static Model parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Model parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Model prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Model}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Model)
        ModelOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return MessageWrapProto.internal_static_com_gylang_sdk_proto_Model_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return MessageWrapProto.internal_static_com_gylang_sdk_proto_Model_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                Model.class, Builder.class);
      }

      // Construct using MessageWrapProto.Model.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        key_ = "";

        content_ = "";

        receiver_ = "";

        type_ = "";

        receiverType_ = "";

        bytes_ = com.google.protobuf.ByteString.EMPTY;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MessageWrapProto.internal_static_com_gylang_sdk_proto_Model_descriptor;
      }

      public Model getDefaultInstanceForType() {
        return Model.getDefaultInstance();
      }

      public Model build() {
        Model result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public Model buildPartial() {
        Model result = new Model(this);
        result.key_ = key_;
        result.content_ = content_;
        result.receiver_ = receiver_;
        result.type_ = type_;
        result.receiverType_ = receiverType_;
        result.bytes_ = bytes_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof Model) {
          return mergeFrom((Model)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(Model other) {
        if (other == Model.getDefaultInstance()) return this;
        if (!other.getKey().isEmpty()) {
          key_ = other.key_;
          onChanged();
        }
        if (!other.getContent().isEmpty()) {
          content_ = other.content_;
          onChanged();
        }
        if (!other.getReceiver().isEmpty()) {
          receiver_ = other.receiver_;
          onChanged();
        }
        if (!other.getType().isEmpty()) {
          type_ = other.type_;
          onChanged();
        }
        if (!other.getReceiverType().isEmpty()) {
          receiverType_ = other.receiverType_;
          onChanged();
        }
        if (other.getBytes() != com.google.protobuf.ByteString.EMPTY) {
          setBytes(other.getBytes());
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        Model parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (Model) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object key_ = "";
      /**
       * <code>optional string key = 1;</code>
       */
      public String getKey() {
        Object ref = key_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public com.google.protobuf.ByteString
          getKeyBytes() {
        Object ref = key_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          key_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public Builder setKey(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        key_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public Builder clearKey() {

        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public Builder setKeyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        key_ = value;
        onChanged();
        return this;
      }

      private Object content_ = "";
      /**
       * <code>optional string content = 2;</code>
       */
      public String getContent() {
        Object ref = content_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          content_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string content = 2;</code>
       */
      public com.google.protobuf.ByteString
          getContentBytes() {
        Object ref = content_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          content_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string content = 2;</code>
       */
      public Builder setContent(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        content_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string content = 2;</code>
       */
      public Builder clearContent() {

        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      /**
       * <code>optional string content = 2;</code>
       */
      public Builder setContentBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        content_ = value;
        onChanged();
        return this;
      }

      private Object receiver_ = "";
      /**
       * <code>optional string receiver = 3;</code>
       */
      public String getReceiver() {
        Object ref = receiver_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          receiver_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string receiver = 3;</code>
       */
      public com.google.protobuf.ByteString
          getReceiverBytes() {
        Object ref = receiver_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          receiver_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string receiver = 3;</code>
       */
      public Builder setReceiver(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        receiver_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string receiver = 3;</code>
       */
      public Builder clearReceiver() {

        receiver_ = getDefaultInstance().getReceiver();
        onChanged();
        return this;
      }
      /**
       * <code>optional string receiver = 3;</code>
       */
      public Builder setReceiverBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        receiver_ = value;
        onChanged();
        return this;
      }

      private Object type_ = "";
      /**
       * <code>optional string type = 4;</code>
       */
      public String getType() {
        Object ref = type_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          type_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string type = 4;</code>
       */
      public com.google.protobuf.ByteString
          getTypeBytes() {
        Object ref = type_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          type_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string type = 4;</code>
       */
      public Builder setType(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string type = 4;</code>
       */
      public Builder clearType() {

        type_ = getDefaultInstance().getType();
        onChanged();
        return this;
      }
      /**
       * <code>optional string type = 4;</code>
       */
      public Builder setTypeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        type_ = value;
        onChanged();
        return this;
      }

      private Object receiverType_ = "";
      /**
       * <code>optional string receiverType = 5;</code>
       */
      public String getReceiverType() {
        Object ref = receiverType_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          receiverType_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string receiverType = 5;</code>
       */
      public com.google.protobuf.ByteString
          getReceiverTypeBytes() {
        Object ref = receiverType_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          receiverType_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string receiverType = 5;</code>
       */
      public Builder setReceiverType(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        receiverType_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string receiverType = 5;</code>
       */
      public Builder clearReceiverType() {

        receiverType_ = getDefaultInstance().getReceiverType();
        onChanged();
        return this;
      }
      /**
       * <code>optional string receiverType = 5;</code>
       */
      public Builder setReceiverTypeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        receiverType_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString bytes_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes bytes = 6;</code>
       */
      public com.google.protobuf.ByteString getBytes() {
        return bytes_;
      }
      /**
       * <code>optional bytes bytes = 6;</code>
       */
      public Builder setBytes(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }

        bytes_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes bytes = 6;</code>
       */
      public Builder clearBytes() {

        bytes_ = getDefaultInstance().getBytes();
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:Model)
    }

    // @@protoc_insertion_point(class_scope:Model)
    private static final Model DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Model();
    }

    public static Model getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Model>
        PARSER = new com.google.protobuf.AbstractParser<Model>() {
      public Model parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new Model(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Model> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Model> getParserForType() {
      return PARSER;
    }

    public Model getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gylang_sdk_proto_Model_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gylang_sdk_proto_Model_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\021MessageWarp.proto\022\024com.gylang.sdk.prot" +
      "o\"j\n\005Model\022\013\n\003key\030\001 \001(\t\022\017\n\007content\030\002 \001(\t" +
      "\022\020\n\010receiver\030\003 \001(\t\022\014\n\004type\030\004 \001(\t\022\024\n\014rece" +
      "iverType\030\005 \001(\t\022\r\n\005bytes\030\006 \001(\014B\022B\020Message" +
      "WrapProtob\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_gylang_sdk_proto_Model_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_gylang_sdk_proto_Model_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gylang_sdk_proto_Model_descriptor,
        new String[] { "Key", "Content", "Receiver", "Type", "ReceiverType", "Bytes", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
