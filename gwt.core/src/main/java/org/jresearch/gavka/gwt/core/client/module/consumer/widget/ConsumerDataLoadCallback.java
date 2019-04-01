package org.jresearch.gavka.gwt.core.client.module.consumer.widget;

import org.jresearch.gavka.rest.data.TopicRestInfo;

@FunctionalInterface
public interface ConsumerDataLoadCallback {

	void onLoad(TopicRestInfo topicInfo);

}
