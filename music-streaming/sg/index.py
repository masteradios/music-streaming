import json
import os
from pathlib import Path

def merge_json_to_array_with_id(input_folder="metadata_json_v1", output_file="music_array_v1.json"):
    """
    Merge all JSON files into a single array with sequential IDs
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
    
    print(f"Found {len(json_files)} JSON files to merge...")
    
    music_array = []
    id_counter = 1
    
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
            
            # Create new document with id field
            music_item = {
                "id": str(id_counter),
                "artistName": document.get("artistName", "Unknown Artist"),
                "createdDate": document.get("createdDate", "Unknown Date"),
                "album": document.get("album", "Unknown Album"),
                "musicName": document.get("musicName", json_file.stem),
                "musicThumbnailUrl": document.get("musicThumbnailUrl", f"sg/thumbnails/{json_file.stem}.jpg"),
                "musicUrl": document.get("musicUrl", f"sg/{json_file.stem}.mp3")
            }
            
            music_array.append(music_item)
            id_counter += 1
            
        except json.JSONDecodeError as e:
            print(f"❌ Error reading {json_file.name}: Invalid JSON - {e}")
        except Exception as e:
            print(f"❌ Error processing {json_file.name}: {e}")
    
    # Write merged array to output file
    if music_array:
        try:
            with open(output_file, 'w', encoding='utf-8') as f:
                json.dump(music_array, f, indent=2, ensure_ascii=False)
            
            print(f"\n✅ Successfully merged {len(music_array)} files!")
            print(f"📄 Output saved as: {output_file}")
            print(f"📊 Array contains {len(music_array)} music items")
            
        except Exception as e:
            print(f"❌ Error writing output file: {e}")
    else:
        print("❌ No valid JSON data found to merge!")

def preview_array_format(input_folder="metadata_json_v1", num_samples=2):
    """
    Preview what the array format will look like
    """
    print("Preview of merged array format:")
    print("=" * 50)
    
    json_files = list(Path(input_folder).glob("*.json"))[:num_samples]
    preview_array = []
    id_counter = 1
    
    for json_file in json_files:
        try:
            # Try utf-8-sig first to handle BOM
            try:
                with open(json_file, 'r', encoding='utf-8-sig') as f:
                    document = json.load(f)
            except UnicodeDecodeError:
                with open(json_file, 'r', encoding='utf-8') as f:
                    document = json.load(f)
            
            music_item = {
                "id": str(id_counter),
                "artistName": document.get("artistName", "Unknown Artist"),

                "album": document.get("album", "Unknown Album"),
                "createdDate": document.get("createdDate", "Unknown Date"),
                "musicName": document.get("musicName", json_file.stem),
                "musicThumbnailUrl": document.get("musicThumbnailUrl", f"sg/thumbnails/{json_file.stem}.jpg"),
                "musicUrl": document.get("musicUrl", f"sg/{json_file.stem}.mp3")
            }
            
            preview_array.append(music_item)
            id_counter += 1
            
        except Exception as e:
            print(f"Error previewing {json_file.name}: {e}")
    
    print(json.dumps(preview_array, indent=2, ensure_ascii=False))
    print("=" * 50)

def validate_json_files(input_folder="metadata_json_v1"):
    """
    Check all JSON files for common issues
    """
    print("Validating JSON files...")
    print("=" * 30)
    
    json_files = list(Path(input_folder).glob("*.json"))
    valid_count = 0
    
    for json_file in json_files:
        try:
            # Try utf-8-sig first to handle BOM
            try:
                with open(json_file, 'r', encoding='utf-8-sig') as f:
                    document = json.load(f)
            except UnicodeDecodeError:
                with open(json_file, 'r', encoding='utf-8') as f:
                    document = json.load(f)
            
            print(f"✅ {json_file.name}")
            valid_count += 1
            
        except json.JSONDecodeError as e:
            print(f"❌ {json_file.name}: JSON Error - {e}")
        except Exception as e:
            print(f"❌ {json_file.name}: {e}")
    
    print(f"\n📊 {valid_count}/{len(json_files)} files are valid")

if __name__ == "__main__":
    print("JSON Array Merger with ID")
    print("=========================")
    
    input_folder = "metadata_json_v1"  # Change if needed
    
    print("\nChoose action:")
    print("1. Merge all JSON files to array")
    print("2. Preview format")
    print("3. Validate JSON files")
    
    choice = input("\nEnter choice (1, 2, or 3, default=1): ").strip()
    
    if choice == "2":
        preview_array_format(input_folder)
    elif choice == "3":
        validate_json_files(input_folder)
    else:
        merge_json_to_array_with_id(input_folder, "music_array_v1.json")
    
    input("\nPress Enter to exit...")