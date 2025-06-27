import json
import os
from pathlib import Path

def create_elasticsearch_bulk_format(input_folder="metadata_json", output_file="bulk_music_data.json"):
    """
    Convert JSON files to Elasticsearch bulk insert format
    Each record becomes two lines:
    1. Index metadata line
    2. Document data line
    """
    
    # Check if input folder exists
    if not os.path.exists(input_folder):
        print(f"❌ Error: Folder '{input_folder}' does not exist!")
        return
    
    # Get all JSON files in the folder
    json_files = list(Path(input_folder).glob("*.json"))
    
    if not json_files:
        print(f"❌ No JSON files found in '{input_folder}' folder!")
        return
    
    print(f"Found {len(json_files)} JSON files to convert...")
    
    bulk_data_lines = []
    doc_id = 1  # Starting ID counter
    
    # Process each JSON file
    for json_file in sorted(json_files):  # Sort for consistent ordering
        try:
            print(f"Processing: {json_file.name}")
            
            # Try utf-8-sig first to handle BOM, fallback to utf-8
            try:
                with open(json_file, 'r', encoding='utf-8-sig') as f:
                    document = json.load(f)
            except UnicodeDecodeError:
                with open(json_file, 'r', encoding='utf-8') as f:
                    document = json.load(f)
            
            # Create the index metadata line
            index_line = {
                "index": {
                    "_index": "music",
                    "_id": str(doc_id)
                }
            }
            
            # Add both lines to our bulk data
            bulk_data_lines.append(json.dumps(index_line, separators=(',', ':')))
            bulk_data_lines.append(json.dumps(document, separators=(',', ':')))
            
            doc_id += 1
            
        except json.JSONDecodeError as e:
            print(f"❌ Error reading {json_file.name}: Invalid JSON - {e}")
        except Exception as e:
            print(f"❌ Error processing {json_file.name}: {e}")
    
    # Write bulk data to output file
    if bulk_data_lines:
        try:
            with open(output_file, 'w', encoding='utf-8') as f:
                for line in bulk_data_lines:
                    f.write(line + '\n')
            
            total_documents = len(bulk_data_lines) // 2
            print(f"\n✅ Successfully converted {total_documents} documents!")
            print(f"📄 Bulk insert file saved as: {output_file}")
            print(f"📊 Total lines in file: {len(bulk_data_lines)}")
            
        except Exception as e:
            print(f"❌ Error writing output file: {e}")
    else:
        print("❌ No valid JSON data found to convert!")

def create_bulk_with_custom_ids(input_folder="metadata_json", output_file="bulk_music_data.json"):
    """
    Alternative version that uses the musicName as the document ID
    """
    
    if not os.path.exists(input_folder):
        print(f"❌ Error: Folder '{input_folder}' does not exist!")
        return
    
    json_files = list(Path(input_folder).glob("*.json"))
    
    if not json_files:
        print(f"❌ No JSON files found in '{input_folder}' folder!")
        return
    
    print(f"Found {len(json_files)} JSON files to convert (using musicName as ID)...")
    
    bulk_data_lines = []
    
    for json_file in sorted(json_files):
        try:
            print(f"Processing: {json_file.name}")
            
            # Try utf-8-sig first to handle BOM, fallback to utf-8
            try:
                with open(json_file, 'r', encoding='utf-8-sig') as f:
                    document = json.load(f)
            except UnicodeDecodeError:
                with open(json_file, 'r', encoding='utf-8') as f:
                    document = json.load(f)
            
            # Use musicName as the document ID (or filename if musicName not available)
            doc_id = document.get('musicName', json_file.stem)
            
            # Create the index metadata line
            index_line = {
                "index": {
                    "_index": "music",
                    "_id": doc_id
                }
            }
            
            # Add both lines to our bulk data
            bulk_data_lines.append(json.dumps(index_line, separators=(',', ':')))
            bulk_data_lines.append(json.dumps(document, separators=(',', ':')))
            
        except json.JSONDecodeError as e:
            print(f"❌ Error reading {json_file.name}: Invalid JSON - {e}")
        except Exception as e:
            print(f"❌ Error processing {json_file.name}: {e}")
    
    if bulk_data_lines:
        try:
            with open(output_file, 'w', encoding='utf-8') as f:
                for line in bulk_data_lines:
                    f.write(line + '\n')
            
            total_documents = len(bulk_data_lines) // 2
            print(f"\n✅ Successfully converted {total_documents} documents!")
            print(f"📄 Bulk insert file saved as: {output_file}")
            print(f"📊 Total lines in file: {len(bulk_data_lines)}")
            
        except Exception as e:
            print(f"❌ Error writing output file: {e}")
    else:
        print("❌ No valid JSON data found to convert!")

def preview_bulk_format(input_folder="metadata_json", num_samples=2):
    """
    Preview what the bulk format will look like
    """
    print("Preview of Elasticsearch bulk format:")
    print("=" * 50)
    
    json_files = list(Path(input_folder).glob("*.json"))[:num_samples]
    doc_id = 1
    
    for json_file in json_files:
        try:
            # Try utf-8-sig first to handle BOM, fallback to utf-8
            try:
                with open(json_file, 'r', encoding='utf-8-sig') as f:
                    document = json.load(f)
            except UnicodeDecodeError:
                with open(json_file, 'r', encoding='utf-8') as f:
                    document = json.load(f)
            
            index_line = {"index": {"_index": "music", "_id": str(doc_id)}}
            
            print(json.dumps(index_line, separators=(',', ':')))
            print(json.dumps(document, separators=(',', ':')))
            
            doc_id += 1
            
        except Exception as e:
            print(f"Error previewing {json_file.name}: {e}")
    
    print("=" * 50)

if __name__ == "__main__":
    print("Elasticsearch Bulk Insert Formatter")
    print("===================================")
    
    input_folder = "metadata_json"  # Change if needed
    
    print("\nChoose ID format:")
    print("1. Sequential IDs (1, 2, 3, ...)")
    print("2. Use musicName as ID")
    print("3. Preview format only")
    
    choice = input("\nEnter choice (1, 2, or 3, default=1): ").strip()
    
    if choice == "3":
        preview_bulk_format(input_folder)
    elif choice == "2":
        create_bulk_with_custom_ids(input_folder, "bulk_music_data.json")
    else:
        create_elasticsearch_bulk_format(input_folder, "bulk_music_data.json")
    
    print("\n💡 Tip: You can now use this file with Elasticsearch's _bulk API:")
    print("   curl -X POST 'localhost:9200/_bulk' -H 'Content-Type: application/json' --data-binary @bulk_music_data.json")
    
    input("\nPress Enter to exit...")