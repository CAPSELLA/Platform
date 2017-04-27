package gr.uoa.di.madgik.csv.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSVParser {

	public Set<String> getAllCSVHeaders(ArrayList<File> files) {
		if (files != null && !files.isEmpty()) {
			Set<String> headers = new HashSet<String>();
			for (File file : files) {
				List<String> headerList = getCSVHeaders(file);
				if (headerList != null)
					headers.addAll(headerList);
			}
			return headers;
		}
		return null;
	}

	public List<String> getCSVHeaders(File file) {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(file));

			// First line in the file is header
			String[] headers = csvReader.readNext();
			csvReader.close();
			if (headers != null && headers.length > 0) {
				List<String> headerList = new ArrayList<String>();
				for (int i = 0; i < headers.length; i++) {
					headerList.add(headers[i]);
				}
				return headerList;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Record> getAllRecords(File file, List<String> keys) {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(file), CSVWriter.DEFAULT_SEPARATOR, '\'', 1);
			List<String[]> line = csvReader.readAll();
			csvReader.close();
			List<Record> records = new ArrayList<Record>();

			for (String[] values : line) {
				Record record = new Record(file.getName());
				for (int i = 0; i < values.length; i++) {
					record.put(keys.get(i).toString(), values[i]);
				}
				records.add(record);
			}
			return records;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeToSingleFile(File output, List<Record> records, Set<String> keys) {
		try {
			CSVWriter csvWriter = new CSVWriter(new FileWriter(output), CSVWriter.DEFAULT_SEPARATOR,
					CSVWriter.NO_QUOTE_CHARACTER);
			String[] headers = keys.toArray(new String[0]);
			csvWriter.writeNext(headers);
			for (Record record : records) {
				List<String> values = new ArrayList<String>();
				for (int i = 0; i < headers.length; i++) {
					values.add(record.getValues().get(headers[i]));

				}
				String[] line = values.toArray(new String[0]);
				csvWriter.writeNext(line);
			}
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
