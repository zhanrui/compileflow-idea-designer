/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.compileflow.idea.graph.codec.impl.tbbpm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.alibaba.compileflow.engine.process.preruntime.converter.impl.parser.model.FlowStreamSource;

/**
 * @author xuan
 * @since 2020/6/22
 */
//public class StringFlowStreamSource implements FlowStreamSource {
public class StringFlowStreamSource extends  FlowStreamSource{

    private String xml;

    public StringFlowStreamSource(String xml) {
        this.xml = xml;
    }

    @Override
    public InputStream getFlow() {
        return new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
    }

}
