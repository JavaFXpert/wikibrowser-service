package com.javafxpert.wikibrowser.util;

/*
 * #%L
 * Wikidata Toolkit Examples
 * %%
 * Copyright (C) 2014 Wikidata Toolkit Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.javafxpert.wikibrowser.WikiBrowserProperties;
import com.javafxpert.wikibrowser.model.conceptmap.ItemRepository;
import com.javafxpert.wikibrowser.model.conceptmap.ItemServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wikidata.wdtk.datamodel.interfaces.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Loads Neo4j database from Wikidata dump
 *
 */
class WikidataNeo4jProcessor implements EntityDocumentProcessor {

	private Log log = LogFactory.getLog(getClass());
	private ItemRepository itemRepository;
	private String language;
	private int onesDigitInt;
	private int startNum;
	private String processType;

	private boolean startNumReached = false;


	public WikidataNeo4jProcessor(ItemRepository itemRepository, String language, int onesDigitInt, int startNum, String processType) {
		this.itemRepository = itemRepository;
		this.language = language;
		this.onesDigitInt = onesDigitInt;
		this.startNum = startNum;
		this.processType = processType;

		if (startNum == 1) {
			startNumReached = true;
		}
	}

	@Override
	public void processItemDocument(ItemDocument itemDocument) {
		ItemIdValue itemIdValue = itemDocument.getItemId();
		String itemId = itemIdValue.getId();
		String itemLabel = itemDocument.findLabel(language);

		if (itemId != null && itemId.length() >= 2 && itemLabel != null && itemLabel.length() > 0) {

			if (itemId.substring(0,1).equalsIgnoreCase("Q")) {
				int qNum = Integer.parseInt(itemId.substring(1));
				if ((qNum % 10) == onesDigitInt && qNum == startNum) {
					startNumReached = true;
				}
				if ((qNum % 10) == onesDigitInt && startNumReached) {
					//log.info("itemId: " + itemId);
					if (!processType.equalsIgnoreCase("relationships")) {

						log.info("====== itemRepository.addItem: " + itemId);

						try {
							itemRepository.addItem(itemId, itemLabel);
						} catch (Exception e) {
							log.error("!!!!!!!!! itemRepository.addItem: " + itemId + ", " + itemLabel + "FAILED!!!!!!");
						}
					}
					else {
						Iterator<Statement> statementsIterator = itemDocument.getAllStatements();
						while (statementsIterator.hasNext()) {
							Statement statement = statementsIterator.next();
							Snak snak = statement.getClaim().getMainSnak();
							String propId = snak.getPropertyId().getId();
							Value value = snak.getValue();
							if (value instanceof ItemIdValue) {
								String valueItemId = ((ItemIdValue) value).getId();
								String propLabel = propId.toLowerCase();

								if (valueItemId != null && valueItemId.length() >= 2 &&
										propId != null && propId.length() > 0 &&
										propLabel != null && propLabel.length() > 0) {

									//TODO: Comment next five lines if all items already exist in DB. Also when using, investigate if item label is available
									/*
									try {
										itemRepository.addItem(valueItemId);
									}
									catch (Exception e) {
										log.error("!!!!!!!!! itemRepository.addItem: " + valueItemId + "FAILED!!!!!!");
									}
									*/

									log.info("------ itemRepository.addRelationship: " + itemId + ", " +
											valueItemId + ", " + propId + ", " + propLabel);
									try {
										itemRepository.addRelationship(itemId, valueItemId, propId,	propLabel);
									} catch (Exception e) {
										log.error("!!!!!!!!! itemRepository.addRelationship: " + itemId + ", " + valueItemId + ", " +
												propId + ", " + propLabel + "FAILED!!!!!!");
									}

								}
							}
						}

					}
				}
			}
		}
	}

	@Override
	public void processPropertyDocument(PropertyDocument propertyDocument) {}

}
