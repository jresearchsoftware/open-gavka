package org.jresearch.gavka.srv;

import java.util.List;

import org.jresearch.gavka.domain.KafkaCluster;

public interface ClusterService {

	List<KafkaCluster> get();
	
	KafkaCluster get(String name);
	
	KafkaCluster save(KafkaCluster cluster);
	
	void delete(String name);
}
