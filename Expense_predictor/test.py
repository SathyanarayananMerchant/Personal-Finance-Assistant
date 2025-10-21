import pandas as pd
from sdv.single_table import GaussianCopulaSynthesizer
from sdv.metadata import Metadata

data = pd.read_csv('BS.csv')
metadata = Metadata.detect_from_dataframe(data)

synthesizer = GaussianCopulaSynthesizer(metadata)
synthesizer.fit(data)
synthetic_data = synthesizer.sample(num_rows=1000)
print(synthetic_data)
