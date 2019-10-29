//Auto generated, do not edit it
//限制！！！
//只做向下兼容（新协议可以解析出老数据，老协议不能解析出新数据）
//字段只能添加，添加后不能删除，字段只能添加到最后
//不能修改字段类型（如从bool改为long）
//${explain}

using System;
using System.Collections.Generic;
<#list imports as importStr>
using Message.${importStr};
</#list>

namespace Message.${name}
{
    public enum TypeEnum
    {
		<#list enumMap?keys as key>
        ${enumMap["${key}"]} = ${key},
		</#list>
    }
	<#assign readMap = {"int":"XBuffer.ReadInt(buffer, ref offset)",
						"long":"XBuffer.ReadLong(buffer, ref offset)",
						"bool":"XBuffer.ReadBool(buffer, ref offset)",
						"string":"XBuffer.ReadString(buffer, ref offset)",
						"short":"XBuffer.ReadShort(buffer, ref offset)",
						"float":"XBuffer.ReadFloat(buffer, ref offset)",
						"double":"XBuffer.ReadDouble(buffer, ref offset)"} >
						
	<#assign writeMap = {"int":"XBuffer.WriteInt(%s, buffer, ref offset)",
						"long":"XBuffer.WriteLong(%s, buffer, ref offset)",
						"bool":"XBuffer.WriteBool(%s, buffer, ref offset)", 
						"string":"XBuffer.WriteString(%s, buffer, ref offset)",
						"short":"XBuffer.WriteShort(%s, buffer, ref offset)",
						"float":"XBuffer.WriteFloat(%s, buffer, ref offset)",
						"double":"XBuffer.WriteDouble(%s, buffer, ref offset)"} >			
						
	<#assign initMap = {"int":"0",
						"long":"0L",
						"bool":"false",
						"string":"null",
						"short":"0",
						"float":"0f",
						"double":"0"} >
						
    <#list structs as struct>
    //${struct.exp}
    <#if struct.superName != "">
    public class ${struct.name} : ${struct.superName}
    <#else>
    public class ${struct.name} : BaseMessage
    </#if>
    {
		<#if struct.cache == "true">
		public override bool doCache { get { return true; } }
		</#if>
		<#if struct.isMessage == true>
        public override int GetMsgId() { return MsgId; }
        public const int MsgId = ${struct.msgId?c};
		</#if>
		
		<#list struct.fields as field>
		<#if field.isList>
        public List<${field.cls}> ${field.name}{get; protected set;} //${field.exp}
		<#elseif field.optional == true>
		private ${field.cls} __${field.name}; // ${field.exp}
		private byte _${field.name} = 0; // ${field.exp} tag
		
		public bool has${field.name?cap_first}()
		{
			return this._${field.name} == 1;
		}
		
		public ${field.cls} ${field.name}
		{
			set
			{
				_${field.name} = 1;
				__${field.name} = value;
			}
			
			get
			{
				return __${field.name};
			}
		}
		<#else> 
		public ${field.cls} ${field.name}; // ${field.exp}
		</#if>
		</#list>

        //构造函数
        public ${struct.name}() : base()
        {
			<#list struct.fields as field>
			<#if field.isList>
			${field.name} = new List<${field.cls}>();
			</#if>
			</#list>
        }
		
		public override void Reset()
		{
			base.Reset();
			<#list struct.fields as field>
			<#if field.isList>
			${field.name}.Clear();
			<#elseif initMap[field.cls]??>
			<#if field.optional == true>
			_${field.name} = 0;
			__${field.name} = ${initMap[field.cls]};
			<#else>
			${field.name} = ${initMap[field.cls]};
			</#if>
			<#else>
			<#if field.optional == true>
			_${field.name} = 0;
			__${field.name} = null;
			<#else>
			${field.name} = null;
			</#if>
			</#if>
			</#list>
		}
		
        //读取数据
        public override int Read(byte[] buffer, int offset)
        {
            try
            {
                offset = base.Read(buffer, offset);
			
				List<bool> _fieldList_ = new List<bool>();
				while(true)
				{
					var _fieldMark_ = XBuffer.ReadShort(buffer, ref offset);
					for(int i = 0; i < 15; ++i)
					{
						bool _mark_ = (_fieldMark_ & 1 << i) == 1;
						if(_mark_) _fieldList_.Add(true);
						else break;
					}
					if((_fieldMark_ & 1 << 15) == 0)
						break;
				}
				int _readIdx_ = 0, _fieldNum_ = _fieldList_.Count;
				
                TypeEnum _real_type_;
                <#list struct.fields as field>
				if(_fieldNum_ > _readIdx_ && !_fieldList_[_readIdx_])
				{
					_readIdx_++;
					<#if field.isList == true>
					short _count_ = XBuffer.ReadShort(buffer, ref offset);
					for(int a = 0; a < _count_; ++a)
					{
					</#if>
						<#if field.optional == true>
						_${field.name} = XBuffer.ReadByte(buffer, ref offset);
						if(_${field.name} == 1)
						{
						</#if>
							<#if !readMap[field.cls]??>
							_real_type_ = (TypeEnum)XBuffer.ReadByte(buffer, ref offset);
							${field.cls} _value_;
							<#if extendCon?keys?seq_contains(field.cls)>
							switch(_real_type_)
							{
								case TypeEnum.${field.cls} : _value_ = new ${field.cls}(); break;
								<#list extendCon["${field.cls}"] as sonCls>
								case TypeEnum.${sonCls} : _value_ = new ${sonCls}(); break;
								</#list>
								default:break;
							}
							<#else>
							<#if field.isList == true>
							${field.name}.Add(new ${field.cls}());
							<#else>
							${field.name} = new ${field.cls}();
							</#if>
							</#if>
							<#else>
							<#if field.isList == true>
							${field.name}.Add(${readMap[field.cls]});
							<#else>
							${field.name} = ${readMap[field.cls]};
							</#if>
							</#if>
						<#if field.optional == true>
						}
						</#if>
					<#if field.isList == true>
					}
					</#if>
				}
				</#list>
            }
            catch(Exception ex)
            {
                throw ex;
            }
			return offset;
        }

		<#if !struct.isMessage>
        public override int WriteWithType(byte[] buffer, int offset)
        {
            XBuffer.WriteByte((byte)TypeEnum.${struct.name}, buffer, ref offset);
            offset = Write(buffer, offset);
			return offset;
        }
		</#if>

        //写入数据
        public override int Write(byte[] buffer, int offset)
        {
            try
            {
                offset = base.Write(buffer, offset);
				
				<#assign fieldNum = struct.fields?size>
				<#assign len = (fieldNum / 15)?floor>
				<#assign lastShort = 0>
				<#if (len > 0)>
				<#list 1 .. len as t>
				XBuffer.WriteShort(Convert.ToInt16(65535), buffer, ref offset);
				</#list>
				<#list (len * 15) + 1 .. fieldNum as t>
					<#assign lastShort = (lastShort * 2) + 1>
				</#list>
				</#if>
				XBuffer.WriteShort(${lastShort?c}, buffer, ref offset);
				
                <#list  struct.fields as field>
				<#if field.isList>
				XBuffer.WriteShort((short)${field.name}.Count, buffer, ref offset);
				for (int a = 0; a < ${field.name}.Count; ++a)
                {
					</#if>
					<#if field.optional == true>
					XBuffer.WriteByte(_${field.name}, buffer, ref offset);
					if(_${field.name} == 1)
					{
					</#if>
						<#if writeMap[field.cls]??>
						<#if field.isList>
						<#assign fun = writeMap[field.cls]>
						${fun?replace("%s", field.name + "[a]")};
						<#else>
						<#assign fun = writeMap[field.cls]>
						${fun?replace("%s", field.name)};
						</#if>
						<#else>
						<#if field.isList>
						if(${field.name}[a] == null)
							UnityEngine.Debug.LogError("${field.name} has nil item, idx == " + a);
						else
							offset = ${field.name}[a].WriteWithType(buffer, offset);
						<#else>
							if(${field.name} == null)
								UnityEngine.Debug.LogError("${field.name} has is null");
							else
								offset = ${field.name}.WriteWithType(buffer, offset);
						</#if>
						</#if>
					<#if field.optional == true>
					}
					</#if>
				<#if field.isList>
				}
				</#if>
                </#list>
            }
            catch(Exception ex)
            {
                throw ex;
            }
			return offset;
        }
    }
    </#list>
}