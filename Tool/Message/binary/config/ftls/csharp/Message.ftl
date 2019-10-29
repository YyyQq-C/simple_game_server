//Auto generated, do not modify it
//���ƣ������������»��߿�ͷ�����ܿ������ռ�̳�
//��������1���ֶ�ֻ����ӣ���Ӻ���ɾ�����ֶ�ֻ����ӵ����
//��������2�������޸��ֶ����ͣ����bool��Ϊlong��
//${explain}

using System;
using System.Collections.Generic;
<#list imports as importStr>
using Message.${importStr};
</#list>

namespace Message.${name}
{
	<#if (enumMap?keys?size > 0)>
    public enum _TypeEnum_
    {
		<#list enumMap?keys as key>
        ${enumMap["${key}"]} = ${key},
		</#list>
    }
	</#if>
	<#assign readMap = {"int":"XBuffer.ReadInt(_buffer_, ref _offset_)",
						"long":"XBuffer.ReadLong(_buffer_, ref _offset_)",
						"bool":"XBuffer.ReadBool(_buffer_, ref _offset_)",
						"string":"XBuffer.ReadString(_buffer_, ref _offset_)",
						"short":"XBuffer.ReadShort(_buffer_, ref _offset_)",
						"float":"XBuffer.ReadFloat(_buffer_, ref _offset_)",
						"double":"XBuffer.ReadDouble(_buffer_, ref _offset_)"} >
						
	<#assign writeMap = {"int":"XBuffer.WriteInt(%s, _buffer_, ref _offset_)",
						"long":"XBuffer.WriteLong(%s, _buffer_, ref _offset_)",
						"bool":"XBuffer.WriteBool(%s, _buffer_, ref _offset_)",
						"string":"XBuffer.WriteString(%s, _buffer_, ref _offset_)",
						"short":"XBuffer.WriteShort(%s, _buffer_, ref _offset_)",
						"float":"XBuffer.WriteFloat(%s, _buffer_, ref _offset_)",
						"double":"XBuffer.WriteDouble(%s, _buffer_, ref _offset_)"} >
						
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
        public List<${field.owner}${field.cls}> ${field.name}{ get; protected set; } //${field.exp}
		<#elseif field.optional == true>
		protected byte _${field.name} = 0; // ${field.exp} tag
		protected ${field.owner}${field.cls} __${field.name}; // ${field.exp}
		public bool has${field.name?cap_first}() { return this._${field.name} == 1; }
		public ${field.owner}${field.cls} ${field.name} { set { _${field.name} = 1; __${field.name} = value; } get { return __${field.name}; } }
		<#else> 
		public ${field.owner}${field.cls} ${field.name}; // ${field.exp}
		</#if>
		</#list>

        //���캯��
        public ${struct.name}() : base()
        {
			<#list struct.fields as field>
			<#if field.isList>
			${field.name} = new List<${field.cls}>();
			</#if>
			</#list>
        }
		
		//��������Բ�����
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
		
        //��ȡ����
        public override int Read(byte[] _buffer_, int _offset_)
        {
            try
            {
                _offset_ = base.Read(_buffer_, _offset_);
			
				<#if !struct.isMessage>
				int _toReadLenOffset_ = _offset_;
				int _toReadLength_ = XBuffer.ReadInt(_buffer_, ref _offset_);
				</#if>
				
				List<bool> _fieldList_ = new List<bool>();
				while(true)
				{
					var _fieldMark_ = XBuffer.ReadByte(_buffer_, ref _offset_);
					for(int i = 0; i < 7; ++i)
					{
						var _h_ = 1 << i;
						bool _mark_ = (_fieldMark_ & _h_) == _h_;
						if(_mark_) _fieldList_.Add(true);
						else break;
					}
					var _e_ = 1 << 7;
					if((_fieldMark_ & _e_) == 0)
						break;
				}
				
				<#if (struct.fields?size > 0)>
				int _fieldNum_ = _fieldList_.Count;
				</#if>
				
				<#assign readIdx = -1>
				while(true)
				{
					<#list struct.fields as field>
					<#assign readIdx = readIdx + 1>
					if(_fieldNum_ > ${readIdx} && _fieldList_[${readIdx}])
					{
						<#if field.isList == true>
						short _count_ = XBuffer.ReadShort(_buffer_, ref _offset_);
						for(int _a_ = 0; _a_ < _count_; ++_a_)
						{
						</#if>
							<#if field.optional == true>
							_${field.name} = XBuffer.ReadByte(_buffer_, ref _offset_);
							if(_${field.name} == 1)
							{
							</#if>
								<#if !readMap[field.cls]??>
								var _real_type_ = (${field.owner}_TypeEnum_)XBuffer.ReadByte(_buffer_, ref _offset_);
								<#if extendCon?keys?seq_contains(field.cls) || importExtendCon?keys?seq_contains(field.cls)>
								${field.owner}${field.cls} _value_ = null;
								switch(_real_type_)
								{
									case ${field.owner}_TypeEnum_.${field.cls} : _value_ = new ${field.owner}${field.cls}(); break;
									<#if extendCon?keys?seq_contains(field.cls)>
									<#list extendCon["${field.cls}"] as sonCls>
									case ${field.owner}_TypeEnum_.${sonCls} : _value_ = new ${field.owner}${sonCls}(); break;
									</#list>
									<#elseif importExtendCon?keys?seq_contains(field.cls)>
									<#list importExtendCon["${field.cls}"] as sonCls>
									case ${field.owner}_TypeEnum_.${sonCls} : _value_ = new ${field.owner}${sonCls}(); break;
									</#list>
									</#if>
									default:break;
								}
								<#else>
								${field.owner}${field.cls} _value_ = new ${field.owner}${field.cls}();
								</#if>
								_offset_ = _value_.Read(_buffer_, _offset_);
								<#if field.isList == true>
								${field.name}.Add(_value_);
								<#else>
								${field.name} = _value_;
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
					} else { break; }
					
					</#list>
					break;
				}
				
				<#if !struct.isMessage>
				//�޳�δ֪����
				while(_offset_ - _toReadLenOffset_ < _toReadLength_)
					XBuffer.ReadByte(_buffer_, ref _offset_);
				</#if>
            }
            catch(Exception ex)
            {
                throw ex;
            }
			return _offset_;
        }

		<#if !struct.isMessage>
        public override int WriteWithType(byte[] _buffer_, int _offset_)
        {
            XBuffer.WriteByte((byte)_TypeEnum_.${struct.name}, _buffer_, ref _offset_);
            _offset_ = Write(_buffer_, _offset_);
			return _offset_;
        }
		</#if>

        //д������
        public override int Write(byte[] _buffer_, int _offset_)
        {
            try
            {
                _offset_ = base.Write(_buffer_, _offset_);
				
				<#if !struct.isMessage>
				int _toWriteLenOffset_ = _offset_;
				XBuffer.WriteInt(0, _buffer_, ref _offset_);
				</#if>
				
				<#assign fieldNum = struct.fields?size>
				<#assign len = (fieldNum / 7)?floor>
				<#assign lastByte = 0>
				<#if (len > 0)>
				<#list 1 .. len as t>
				XBuffer.WriteByte(255, _buffer_, ref _offset_);
				</#list>
				</#if>
				<#if (fieldNum > 0)>
				<#list (len * 7) + 1 .. fieldNum as t>
				<#assign lastByte = (lastByte * 2) + 1>
				</#list>
				</#if>
				XBuffer.WriteByte(${lastByte?c}, _buffer_, ref _offset_);
				
				<#assign _lc_ = false>
                <#list struct.fields as field>
				<#if field.isList>
				
				<#if !_lc_>short </#if>_listCount_ = (short)${field.name}.Count;
				<#assign _lc_ = true>
				XBuffer.WriteShort(_listCount_, _buffer_, ref _offset_);
				for (int _a_ = 0; _a_ < _listCount_; ++_a_)
                {
				</#if>
					<#if field.optional == true>
					XBuffer.WriteByte(_${field.name}, _buffer_, ref _offset_);
					if(_${field.name} == 1)
					{
					</#if>
						<#if writeMap[field.cls]??>
						<#if field.isList>
						<#assign fun = writeMap[field.cls]>
						${fun?replace("%s", field.name + "[_a_]")};
						<#else>
						<#assign fun = writeMap[field.cls]>
						${fun?replace("%s", field.name)};
						</#if>
						<#else>
						<#if field.isList>
						if(${field.name}[_a_] == null)
							UnityEngine.Debug.LogError("${field.name} has nil item, idx == " + _a_);
						else
							_offset_ = ${field.name}[_a_].WriteWithType(_buffer_, _offset_);
						<#else>
							if(${field.name} == null)
								UnityEngine.Debug.LogError("${field.name} is null");
							else
								_offset_ = ${field.name}.WriteWithType(_buffer_, _offset_);
						</#if>
						</#if>
					<#if field.optional == true>
					}
					</#if>
				<#if field.isList>
				}
				</#if>
                </#list>
				
				<#if !struct.isMessage>
				XBuffer.WriteInt(_offset_ - _toWriteLenOffset_, _buffer_, ref _toWriteLenOffset_);
				</#if>
            }
            catch(Exception ex)
            {
                throw ex;
            }
			return _offset_;
        }
    }
    </#list>
}