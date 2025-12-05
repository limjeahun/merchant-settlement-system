package com.settlement.worker.adapter.out.persistence.mongo.repository

import com.settlement.mss.worker.adapter.out.persistence.mongo.document.ReportDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface ReportMongoRepository : MongoRepository<ReportDocument, String> {

}