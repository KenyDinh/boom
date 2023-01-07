package dev.boom.core;

import org.apache.click.service.XmlConfigService;

public class BoomConfigService extends XmlConfigService {

	@Override
	public boolean isTemplate(String path) {
		boolean isTemplate = super.isTemplate(path);

		if (!isTemplate) {
			if (path.endsWith(".xml") || path.endsWith(".json") || path.endsWith(".csv")) {
				isTemplate = true;
			}
		}
		return isTemplate;
	}

}
